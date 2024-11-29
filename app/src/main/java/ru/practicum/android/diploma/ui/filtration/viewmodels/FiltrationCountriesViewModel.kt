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

class FiltrationCountriesViewModel(
    private val countryGetter: AreasInteractor,
    private val filterSetter: SetSearchFilterInteractor,
    private val loadingStatus: GetDataLoadingStatusUseCase
) : ViewModel() {
    private val _liveData = XxxLiveData<FiltrationCountryData>()
    val liveData: LiveData<FiltrationCountryData> get() = _liveData
    private var job: Job? = null

    init {
        viewModelScope.launch {

            job = launch {
                loadingStatus().collect { status ->
                    when (status) {
                        DataLoadingStatus.APP_FIRST_START -> Unit

                        DataLoadingStatus.NO_INTERNET -> {
                            _liveData.postValue(FiltrationCountryData.NoInternet)
                        }

                        DataLoadingStatus.LOADING -> {
                            _liveData.postValue(FiltrationCountryData.Loading)
                        }

                        DataLoadingStatus.SERVER_ERROR -> {
                            _liveData.postValue(FiltrationCountryData.NotFoundCountries)
                        }

                        DataLoadingStatus.COMPLETE -> {
                            job?.cancel()
                        }
                    }
                }
            }
            job?.join()

            // получить страны можно только после того как они были загружены в БД
            getCountries()
        }
    }

    private fun getCountries() {
        viewModelScope.launch {
            val countries = countryGetter.getAllCountries()
            _liveData.postValue(FiltrationCountryData.Countries(countries))
        }
    }

    fun setArea(area: Area) {
        viewModelScope.launch {
            filterSetter.saveAreaTempValue(area)
            _liveData.postValue(
                FiltrationCountryData.GoBack
            )
        }
    }
}
