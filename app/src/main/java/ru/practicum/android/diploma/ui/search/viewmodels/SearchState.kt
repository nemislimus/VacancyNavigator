package ru.practicum.android.diploma.ui.search.viewmodels

import ru.practicum.android.diploma.domain.models.VacancyShort

sealed interface SearchState {

    data object IsLoadingNextPage : SearchState

    data object IsLoading : SearchState

    data class ConnectionError(val replaceVacancyList: Boolean) : SearchState

    data class NotFoundError(val replaceVacancyList: Boolean) : SearchState

    data class ServerError500(val replaceVacancyList: Boolean) : SearchState

    data class Content(val pageData: List<VacancyShort>, val listNeedsScrollTop: Boolean) : SearchState

    data class VacanciesCount(val vacanciesCount: Int) : SearchState

    data class QueryIsEmpty(val isEmpty: Boolean) : SearchState

    data class SearchText(val text: String) : SearchState
}
