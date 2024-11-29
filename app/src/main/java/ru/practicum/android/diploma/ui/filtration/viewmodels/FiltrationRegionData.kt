package ru.practicum.android.diploma.ui.filtration.viewmodels

import ru.practicum.android.diploma.domain.models.Area

sealed interface FiltrationRegionData {
    data class Regions(val regions: List<Area>) : FiltrationRegionData
    data class GoBack(val region: Area?) : FiltrationRegionData
    data object IncorrectRegion : FiltrationRegionData
    data object NotFoundRegion : FiltrationRegionData
    data object NoInternet : FiltrationRegionData
    data object Loading : FiltrationRegionData
}
