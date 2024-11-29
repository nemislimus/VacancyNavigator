package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.repository.AreasInteractor
import ru.practicum.android.diploma.domain.repository.GetDataLoadingStatusUseCase
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor

class FiltrationCityViewModel(
    regionsGetter: AreasInteractor,
    filterSetter: SetSearchFilterInteractor,
    parentId: String?,
    loadingStatus: GetDataLoadingStatusUseCase
) : FiltrationRegionViewModel(regionsGetter, filterSetter, parentId, loadingStatus) {
    override fun getRegions(search: String?) {
        userSearchQuery = search
        if (!hasRegionsList) {
            // пока регионы не загружены поиск по ним не имеет смысла
            // о том что они получены мы узнаем от родителя который выставит hasRegionsList = true
            return
        }
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
                xxxLiveData.postValue(FiltrationRegionData.IncorrectRegion)
            } else {
                xxxLiveData.postValue(FiltrationRegionData.Regions(regions))
            }
        }
    }
}
