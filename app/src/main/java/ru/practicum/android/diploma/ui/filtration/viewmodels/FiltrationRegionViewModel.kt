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
    var isRegionsLoaded: Boolean = false
    var isNextPageLoading = false
    val liveData: LiveData<FiltrationRegionData> get() = xxxLiveData
    var lastSearchQuery: String? = null
    var maxPages = Integer.MAX_VALUE
    var currentPage = 0
    val areasList: MutableList<Area> = mutableListOf()
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
                            isRegionsLoaded = true
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
            getRegions(lastSearchQuery)
        }
    }

    open fun getRegions(search: String? = null) {
        if (search != lastSearchQuery) {
            clearPagingHistory()
            lastSearchQuery = search
        }

        if (!(isRegionsLoaded && currentPage < maxPages)) return

        viewModelScope.launch {
            val regions = if (parentId.isNullOrBlank()) {
                regionsGetter.getAllRegions(search, currentPage)
            } else {
                regionsGetter.getRegionsInCountry(parentId, search, currentPage)
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
            isNextPageLoading = false
        }
    }

    fun saveRegion(region: Area) {
        viewModelScope.launch {
            filterSetter.saveAreaTempValue(region)
            xxxLiveData.setSingleEventValue(FiltrationRegionData.GoBack(region))
        }
    }

    fun clearPagingHistory() {
        areasList.clear()
        maxPages = Integer.MAX_VALUE
        currentPage = 0
    }

    fun setNoScrollOnViewCreated() {
        xxxLiveData.setStartValue(FiltrationRegionData.Regions(areasList, false))
    }

    fun onLastItemReached() {
        if (!isNextPageLoading) {
            isNextPageLoading = true
            getRegions(lastSearchQuery)
        }
    }
}
