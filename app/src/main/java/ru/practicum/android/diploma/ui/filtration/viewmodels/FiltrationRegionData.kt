package ru.practicum.android.diploma.ui.filtration.viewmodels

import ru.practicum.android.diploma.domain.models.Area

sealed interface FiltrationRegionData {
    data class Regions(val regions: List<Area>) : FiltrationRegionData
    data class GoBack(val region: Area?) : FiltrationRegionData
    data object NotSuchRegion : FiltrationRegionData
    data object NotFound : FiltrationRegionData
}
