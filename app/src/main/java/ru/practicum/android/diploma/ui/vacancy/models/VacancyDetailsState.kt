package ru.practicum.android.diploma.ui.vacancy.models

import ru.practicum.android.diploma.domain.models.VacancyFull

sealed interface VacancyDetailsState {
    data object Loading : VacancyDetailsState

    data class Content(
        val vacancy: VacancyFull
    ) : VacancyDetailsState

    data class ServerError(
        val errorMessage: String
    ) : VacancyDetailsState

    data class EmptyResult(
        val emptyMessage: String
    ) : VacancyDetailsState

    data class NoConnection(
        val errorMessage: String
    ) : VacancyDetailsState
}
