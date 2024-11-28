package ru.practicum.android.diploma.ui.filtration.viewmodels

import ru.practicum.android.diploma.domain.models.Industry

sealed interface FiltrationIndustryData {
    data class Industries(val industries: List<Industry>) : FiltrationIndustryData
    data object GoBack : FiltrationIndustryData
    data object NotFoundIndustry : FiltrationIndustryData
}
