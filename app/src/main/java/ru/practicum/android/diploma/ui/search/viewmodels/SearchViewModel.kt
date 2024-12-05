package ru.practicum.android.diploma.ui.search.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.domain.repository.GetSearchFilterInteractor
import ru.practicum.android.diploma.domain.search.api.SearchInteractor
import ru.practicum.android.diploma.ui.utils.XxxLiveData
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val filterGetter: GetSearchFilterInteractor,
) : ViewModel() {

    private var currentPage: Int = 0
    private var maxPages: Int = Int.MAX_VALUE
    private val vacanciesList: MutableList<VacancyShort> = mutableListOf()
    private var isNextPageLoading = false

    private val _filterState: MutableLiveData<Boolean> = MutableLiveData(false)
    internal val filterState: LiveData<Boolean> get() = _filterState

    private val _searchState: XxxLiveData<SearchState> = XxxLiveData()
    internal val searchState: LiveData<SearchState> get() = _searchState

    private val _searchDebounce: (String) -> Unit =
        debounce(true, viewModelScope, SEARCH_DEBOUNCE_DELAY) { searchText ->
            searchVacancies(searchText)
        }

    private var lastSearchRequest: String = ""
    private var searchJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.Main) {
            filterGetter.isFilterExists().collect { existOrNotExist ->
                _filterState.postValue(existOrNotExist)
            }
        }
    }

    fun searchDebounce(searchQuery: String) {
        if (searchQuery == lastSearchRequest) {
            return
        }
        if (searchQuery.isBlank()) {
            cancelSearch()
            _searchState.setValue(SearchState.QueryIsEmpty(isEmpty = true))
        } else {
            searchJob?.cancel()
            if (lastSearchRequest.isBlank()) {
                _searchState.setValue(SearchState.QueryIsEmpty(isEmpty = false))
            }
        }
        clearPagingHistory()
        lastSearchRequest = searchQuery
        _searchDebounce(searchQuery)
        _searchState.setStartValue(SearchState.SearchText(searchQuery))
    }

    fun forceSearchLastRequest() {
        if (lastSearchRequest.isNotBlank()) {
            clearPagingHistory()
            clearLiveData()
            searchVacancies(lastSearchRequest)
        }
    }

    private fun clearPagingHistory() {
        vacanciesList.clear()
        maxPages = Integer.MAX_VALUE
        currentPage = 0
    }

    private fun searchVacancies(searchQuery: String) {
        if (searchQuery.isNotEmpty() && currentPage < maxPages) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                runCatching {
                    searchInteractor.searchVacancy(searchQuery, currentPage).collect { result ->
                        withContext(Dispatchers.Main) {
                            val replaceVacancyList = currentPage == 0
                            when (result) {
                                is Resource.ConnectionError -> {
                                    if (currentPage == 0) {
                                        renderState(SearchState.ConnectionError(true), true)
                                    } else {
                                        _searchState.setSingleEventValue(SearchState.ConnectionError(false))
                                    }
                                }

                                is Resource.NotFoundError -> renderState(
                                    SearchState.NotFoundError(replaceVacancyList),
                                    replaceVacancyList
                                )

                                is Resource.ServerError -> renderState(
                                    SearchState.ServerError500(replaceVacancyList),
                                    replaceVacancyList
                                )

                                is Resource.Success -> {
                                    with(result.data) {
                                        isNextPageLoading = false
                                        maxPages = pages
                                        if (found > 0) {
                                            vacanciesList.addAll(items)
                                            renderState(SearchState.Content(vacanciesList, currentPage == 0), true)
                                            renderState(SearchState.VacanciesCount(found))
                                            ++currentPage
                                        } else if (currentPage == 0) {
                                            renderState(SearchState.NotFoundError(true), true)
                                        } else {
                                            Unit
                                        }
                                    }
                                }

                                is Resource.Loading -> renderLoadingState()
                            }
                        }
                    }
                }.onFailure { er ->
                    Log.d("WWW", "Vacancy search error: $er")
                }
            }
        }
    }

    fun setNoScrollOnViewCreated() {
        _searchState.setStartValue(SearchState.Content(vacanciesList, false))
    }

    private fun renderLoadingState() {
        if (currentPage == 0) {
            renderState(SearchState.IsLoading, true)
        } else {
            _searchState.setSingleEventValue(SearchState.IsLoadingNextPage)
        }
    }

    private fun clearLiveData(){
        _searchState.clear()
        _searchState.setValue(
            SearchState.QueryIsEmpty(
                isEmpty = lastSearchRequest.isBlank()
            )
        )
        _searchState.setStartValue(SearchState.SearchText(lastSearchRequest))
    }

    private fun renderState(newState: SearchState, clearOtherStates: Boolean = false) {
        if (clearOtherStates) {
            clearLiveData()
        }
        _searchState.setValue(newState)
    }

    fun onLastItemReached() {
        if (!isNextPageLoading) {
            isNextPageLoading = true
            searchVacancies(lastSearchRequest)
        }
    }

    fun cancelSearch() {
        searchJob?.cancel()
        clearLiveData()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
