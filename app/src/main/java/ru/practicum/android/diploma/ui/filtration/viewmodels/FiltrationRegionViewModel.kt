package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.DataLoadingStatus
import ru.practicum.android.diploma.domain.repository.AreasInteractor
import ru.practicum.android.diploma.domain.repository.GetDataLoadingStatusUseCase
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor
import ru.practicum.android.diploma.ui.utils.XxxLiveData

open class FiltrationRegionViewModel(
    val regionsGetter: AreasInteractor,
    private val filterSetter: SetSearchFilterInteractor,
    private val parentId: String?,
    private val loadingStatus: GetDataLoadingStatusUseCase
) : ViewModel() {
    val xxxLiveData = XxxLiveData<FiltrationRegionData>()
    var parentArea: Area? = null
    var hasRegionsList: Boolean = false
    val liveData: LiveData<FiltrationRegionData> get() = xxxLiveData
    var userSearchQuery: String? = null
    private var job: Job? = null

    init {
        viewModelScope.launch {

            job = launch {
                loadingStatus().collect { status ->
                    when (status) {
                        DataLoadingStatus.APP_FIRST_START -> Unit

                        DataLoadingStatus.NO_INTERNET -> {
                            xxxLiveData.postValue(FiltrationRegionData.NoInternet)
                        }

                        DataLoadingStatus.LOADING -> {
                            xxxLiveData.postValue(FiltrationRegionData.Loading)
                        }

                        DataLoadingStatus.SERVER_ERROR -> {
                            xxxLiveData.postValue(FiltrationRegionData.NotFoundRegion)
                        }

                        DataLoadingStatus.COMPLETE -> {
                            hasRegionsList = true
                            job?.cancel()
                        }
                    }
                }
            }
            job?.join()

            // получить регионы можно только после того как они были загружены в БД
            parentId?.let {
                parentArea = regionsGetter.getAreaById(parentId)
            }
            getRegions(userSearchQuery)
        }
    }

    open fun getRegions(search: String? = null) {
        userSearchQuery = search
        if (!hasRegionsList) return

        viewModelScope.launch {
            val regions = if (parentId.isNullOrBlank()) {
                regionsGetter.getAllRegions(search)
            } else {
                regionsGetter.getRegionsInCountry(parentId, search)
            }

            if (regions.isEmpty()) {
                xxxLiveData.postValue(FiltrationRegionData.IncorrectRegion)
            } else {
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
