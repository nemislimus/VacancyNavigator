package ru.practicum.android.diploma.ui.favorites.viewmodels

import ru.practicum.android.diploma.domain.models.VacancyShort

sealed interface FavoritesState {
    data class Vacancies(val list: List<VacancyShort>) : FavoritesState
    data object DbFail : FavoritesState
}
