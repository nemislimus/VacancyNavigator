package ru.practicum.android.diploma.ui.filtration.viewmodels

import ru.practicum.android.diploma.domain.models.SearchFilter

sealed interface FiltrationData {
    data class Filter(val filter: SearchFilter) : FiltrationData
    data class ApplyButton(val visible: Boolean) : FiltrationData
    data class ResetButton(val visible: Boolean) : FiltrationData
    data class GoBack(val applyBeforeExiting: Boolean) : FiltrationData
}
