package ru.practicum.android.diploma.ui.search.viewmodels

import ru.practicum.android.diploma.domain.models.VacancyShort

sealed interface SearchState {

    data object IsLoadingNextPage : SearchState

    data object IsLoading : SearchState

    data object ConnectionError : SearchState

    data object NotFoundError : SearchState

    data class Content(val pageData: List<VacancyShort>, val vacanciesCount: Int) : SearchState
}
