package ru.practicum.android.diploma.ui.filtration.viewmodels

import ru.practicum.android.diploma.domain.models.SearchFilter

sealed interface FiltrationData {
    data class Filter(val filter: SearchFilter) : FiltrationData
    data class IsFilterChanged(val isChanged: Boolean) : FiltrationData
    data class GoBack(val applyBeforeExiting: Boolean) : FiltrationData
}
