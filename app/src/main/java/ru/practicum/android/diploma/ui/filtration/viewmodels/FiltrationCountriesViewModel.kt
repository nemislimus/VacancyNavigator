package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.repository.AreasInteractor
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor
import ru.practicum.android.diploma.ui.utils.XxxLiveData

class FiltrationCountriesViewModel(
    private val countryGetter: AreasInteractor,
    private val filterSetter: SetSearchFilterInteractor
) : ViewModel() {
    private val _liveData = XxxLiveData<FiltrationCountryData>()
    val liveData: LiveData<FiltrationCountryData> get() = _liveData
    init {
        viewModelScope.launch {
            getCountries()
        }
    }

    private fun getCountries() {
        viewModelScope.launch {
            _liveData.postValue(
                FiltrationCountryData.Countries(
                    countryGetter.getAllCountries()
                )
            )
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
