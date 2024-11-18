package ru.practicum.android.diploma.ui.search.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.domain.search.api.SearchInteractor
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private var currentPage: Int = 0
    private var maxPages: Int = Int.MAX_VALUE
    private val vacanciesList: MutableList<VacancyShort> = mutableListOf()
    private var isNextPageLoading = false

    private val _searchState: MutableLiveData<SearchState> = MutableLiveData()
    internal val searchState: LiveData<SearchState> get() = _searchState

    private val _searchDebounce: (String) -> Unit =
        debounce(true, viewModelScope, SEARCH_DEBOUNCE_DELAY) { searchText ->
            searchVacancies(searchText)
        }

    private var lastSearchRequest: String = ""

    fun searchDebounce(searchQuery: String) {
        if (searchQuery == lastSearchRequest) {
            return
        }
        clearPagingHistory()
        lastSearchRequest = searchQuery

        _searchDebounce(searchQuery)

    }

    private fun clearPagingHistory() {
        vacanciesList.clear()
        currentPage = 0
    }

    private fun searchVacancies(searchQuery: String) {
        if (searchQuery.isNotEmpty() && currentPage < maxPages) {
            if (currentPage == 0) {
                renderState(SearchState.IsLoading)
            } else {
                renderState(SearchState.IsLoadingNextPage)
            }
            viewModelScope.launch {
                searchInteractor.searchVacancy(searchQuery, currentPage).collect { result ->
                    when (result) {
                        is Resource.ConnectionError -> renderState(SearchState.ConnectionError)
                        is Resource.NotFoundError -> renderState(SearchState.NotFoundError)
                        is Resource.ServerError -> renderState(SearchState.NotFoundError)
                        is Resource.Success -> {
                            ++currentPage
                            isNextPageLoading = false
                            if (result.data.found > 0) {
                                vacanciesList.addAll(result.data.items)
                                renderState(SearchState.Content(vacanciesList))
                            } else {
                                renderState(SearchState.NotFoundError)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun renderState(newState: SearchState) {
        _searchState.value = newState
    }

    fun onLastItemReached() {
        if (!isNextPageLoading) {
            isNextPageLoading = true
            searchVacancies(lastSearchRequest)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
