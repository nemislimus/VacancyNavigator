package ru.practicum.android.diploma.ui.filtration.viewmodels

import ru.practicum.android.diploma.domain.models.SearchFilter

sealed interface FiltrationPlaceOfWorkData {
    data class OldFilter(val filter: SearchFilter) : FiltrationPlaceOfWorkData
    data class NewFilter(val filter: SearchFilter) : FiltrationPlaceOfWorkData
    data object GoBack : FiltrationPlaceOfWorkData
}
