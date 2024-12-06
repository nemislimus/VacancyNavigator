package ru.practicum.android.diploma.ui.filtration.viewmodels

import ru.practicum.android.diploma.domain.models.Area

sealed interface FiltrationCountryData {
    data class Countries(val countries: List<Area>) : FiltrationCountryData
    data object GoBack : FiltrationCountryData
    data object NotFoundCountries : FiltrationCountryData
    data object NoInternet : FiltrationCountryData
    data object Loading : FiltrationCountryData
}
