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
        if (search != lastSearchQuery) {
            clearPagingHistory()
            lastSearchQuery = search
        }

        if (!(isRegionsLoaded && currentPage < maxPages)) return

        viewModelScope.launch {
            var regions: List<Area> = emptyList()
            parentArea?.let { area ->
                area.parentId?.let {
                    regions = regionsGetter.getCitiesInRegion(area.id, search, currentPage)
                } ?: run {
                    regions = regionsGetter.getCitiesInCountry(area.id, search, currentPage)
                }
            } ?: run {
                regions = regionsGetter.getAllCities(search, currentPage)
            }

            if (regions.isEmpty()) {
                if (currentPage == 0) {
                    xxxLiveData.postValue(FiltrationRegionData.IncorrectRegion)
                } else {
                    maxPages = currentPage
                }
            } else {
                areasList.addAll(regions)
                xxxLiveData.postValue(FiltrationRegionData.Regions(areasList, currentPage == 0))
            }
            currentPage++
        }
    }
}
