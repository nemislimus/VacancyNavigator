package ru.practicum.android.diploma.ui.search.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.search.api.SearchInteractor
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val _searchState: MutableLiveData<SearchState> = MutableLiveData()
    internal val searchState: LiveData<SearchState> get() = _searchState

    private val _searchDebounce: (String) -> Unit =
        debounce(true, viewModelScope, SEARCH_DEBOUNCE_DELAY) { searchText ->
            searchVacancy(searchText)
        }

    private var lastSearchRequest: String = ""

    fun searchDebounce(searchQuery: String) {
        if (searchQuery == lastSearchRequest) {
            return
        }
        lastSearchRequest = searchQuery

        _searchDebounce(searchQuery)

    }

    private fun searchVacancy(searchQuery: String) {
        if (searchQuery.isNotEmpty()) {
            renderState(SearchState.IsLoading)
            viewModelScope.launch {
                searchInteractor.searchVacancy(searchQuery, 0).collect { result ->
                    when (result) {
                        is Resource.ConnectionError -> renderState(SearchState.ConnectionError)
                        is Resource.NotFoundError -> renderState(SearchState.NotFoundError)
                        is Resource.ServerError -> renderState(SearchState.NotFoundError)
                        is Resource.Success -> {
                            if (result.data.found > 0) {
                                renderState(SearchState.Content(result.data.items))
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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
