package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.DataLoadingStatus
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.repository.GetDataLoadingStatusUseCase
import ru.practicum.android.diploma.domain.repository.GetSearchFilterInteractor
import ru.practicum.android.diploma.domain.repository.IndustriesInteractor
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor

class FiltrationIndustryViewModel(
    private val industriesGetter: IndustriesInteractor,
    private val filterSetter: SetSearchFilterInteractor,
    private val filterGetter: GetSearchFilterInteractor,
    private val loadingStatus: GetDataLoadingStatusUseCase
) : ViewModel() {
    private val liveData = MutableLiveData<FiltrationIndustryData>()
    private var selectedIndustry: Industry? = null
    private var hasIndustriesList: Boolean = false
    private var job: Job? = null

    init {
        viewModelScope.launch {

            job = launch {
                loadingStatus().collect { status ->
                    when (status) {
                        DataLoadingStatus.APP_FIRST_START -> Unit

                        DataLoadingStatus.NO_INTERNET -> {
                            liveData.postValue(FiltrationIndustryData.NoInternet)
                        }

                        DataLoadingStatus.LOADING -> {
                            liveData.postValue(FiltrationIndustryData.Loading)
                        }

                        DataLoadingStatus.SERVER_ERROR -> {
                            liveData.postValue(FiltrationIndustryData.NotFoundIndustry)
                        }

                        DataLoadingStatus.COMPLETE -> {
                            hasIndustriesList = true
                            job?.cancel()
                        }
                    }
                }
            }
            job?.join()

            filterGetter.getFilter().first().let { filter ->
                selectedIndustry = filter?.industry
            }

            // получить отрасли можно только после того как они были загружены в БД
            getIndustries()
        }
    }

    fun getLiveData(): LiveData<FiltrationIndustryData> = liveData

    fun getIndustries(search: String? = null) {
        if (!hasIndustriesList) return

        viewModelScope.launch {
            val industriesList = industriesGetter.getAllIndustries(search)

            if (industriesList.isEmpty()) {
                liveData.postValue(FiltrationIndustryData.IncorrectIndustry)
            } else {
                industriesList.toMutableList().let { industries ->
                    industries.sortBy { it.name }
                    selectedIndustry?.let {
                        industries.forEachIndexed { index, industry ->
                            if (selectedIndustry == industry) {
                                industries[index] = industry.copy(isSelected = true)
                            }
                        }
                    }
                    liveData.postValue(
                        FiltrationIndustryData.Industries(
                            industries = industries.partition { it.isSelected }.let { it.first + it.second }
                        )
                    )
                }
            }
        }
    }

    fun setIndustry(industry: Industry) {
        selectedIndustry = industry.copy(isSelected = false)
    }

    fun saveIndustryFilter() {
        viewModelScope.launch {
            filterSetter.saveIndustry(selectedIndustry)
            liveData.postValue(FiltrationIndustryData.GoBack)
        }
    }
}
