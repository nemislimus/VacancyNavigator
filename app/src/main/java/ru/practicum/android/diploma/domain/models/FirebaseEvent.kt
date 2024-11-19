package ru.practicum.android.diploma.domain.models

sealed interface FirebaseEvent {
    data class ViewScreen(val name: String) : FirebaseEvent
    data class Log(val message: String) : FirebaseEvent
    data class Error(val message: String) : FirebaseEvent
    data class SearchVacancy(val text: String, val filter: SearchFilter) : FirebaseEvent
    data class AddToFavorite(val vacancy: VacancyFull) : FirebaseEvent
}
