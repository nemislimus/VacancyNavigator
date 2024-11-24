package ru.practicum.android.diploma.ui.filtration.viewmodels

import ru.practicum.android.diploma.domain.models.Area

sealed interface FiltrationRegionData {
    data class Regions(val regions: List<Area>) : FiltrationRegionData
    data object GoBack : FiltrationRegionData
    data object NotSuchRegion : FiltrationRegionData
    data object NotFound : FiltrationRegionData
}
