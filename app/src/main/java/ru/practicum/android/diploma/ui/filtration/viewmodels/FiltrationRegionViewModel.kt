package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.repository.AreasInteractor
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor
import ru.practicum.android.diploma.ui.utils.XxxLiveData

open class FiltrationRegionViewModel(
    val regionsGetter: AreasInteractor,
    private val filterSetter: SetSearchFilterInteractor,
    private val parentId: String?
) : ViewModel() {
    val xxxLiveData = XxxLiveData<FiltrationRegionData>()
    var parentArea: Area? = null
    private var hasRegionsList: Boolean = false
    val liveData: LiveData<FiltrationRegionData> get() = xxxLiveData

    init {
        viewModelScope.launch {
            parentId?.let {
                parentArea = regionsGetter.getAreaById(parentId)
            }
            getRegions()
        }
    }

    open fun getRegions(search: String? = null) {
        viewModelScope.launch {
            val regions = if (parentId.isNullOrBlank()) {
                regionsGetter.getAllRegions(search)
            } else {
                regionsGetter.getRegionsInCountry(parentId, search)
            }

            if (regions.isEmpty()) {
                if (hasRegionsList) {
                    xxxLiveData.postValue(FiltrationRegionData.IncorrectRegion)
                } else {
                    xxxLiveData.postValue(FiltrationRegionData.NotFoundRegion)
                }
            } else {
                hasRegionsList = true
                xxxLiveData.postValue(FiltrationRegionData.Regions(regions))
            }
        }
    }

    fun saveRegion(region: Area) {
        viewModelScope.launch {
            filterSetter.saveAreaTempValue(region)
            xxxLiveData.setSingleEventValue(FiltrationRegionData.GoBack(region))
        }
    }
}
