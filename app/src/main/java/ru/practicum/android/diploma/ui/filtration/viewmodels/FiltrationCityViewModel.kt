package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.repository.AreasInteractor
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor

class FiltrationCityViewModel(
    regionsGetter: AreasInteractor,
    filterSetter: SetSearchFilterInteractor,
    parentId: String?
) : FiltrationRegionViewModel(regionsGetter, filterSetter, parentId) {
    override fun getRegions(search: String?) {
        viewModelScope.launch {
            var regions: List<Area> = emptyList()
            parentArea?.let { area ->
                area.parentId?.let {
                    regions = regionsGetter.getCitiesInRegion(area.id, search)
                } ?: run {
                    regions = regionsGetter.getCitiesInCountry(area.id, search)
                }
            } ?: run {
                regions = regionsGetter.getAllCities(search)
            }

            if (regions.isEmpty()) {
                xxxLiveData.postValue(FiltrationRegionData.NotFound)
            } else {
                xxxLiveData.postValue(FiltrationRegionData.Regions(regions))
            }
        }
    }
}
