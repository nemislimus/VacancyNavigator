package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.repository.AreasInteractor
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor
import ru.practicum.android.diploma.ui.utils.XxxLiveData

class FiltrationRegionViewModel(
    private val regionsGetter: AreasInteractor,
    private val filterSetter: SetSearchFilterInteractor,
    private val countryId: String?,
) : ViewModel() {
    private val _liveData = XxxLiveData<FiltrationRegionData>()
    val liveData: LiveData<FiltrationRegionData> get() = _liveData

    init {
        viewModelScope.launch {
            getRegions()
        }
    }

    fun getRegions(search: String? = null) {
        viewModelScope.launch {
            val regions = if (countryId.isNullOrBlank()) {
                regionsGetter.getAllRegions(search)
            } else {
                regionsGetter.getRegionsInCountry(countryId, search)
            }

            if (regions.isEmpty()) {
                _liveData.postValue(FiltrationRegionData.NotFound)
            } else {
                _liveData.postValue(FiltrationRegionData.Regions(regions))
            }
        }
    }

    fun saveRegion(region: Area) {
        viewModelScope.launch {
            filterSetter.saveAreaTempValue(region)
            _liveData.setSingleEventValue(FiltrationRegionData.GoBack(region))
        }
    }
}
