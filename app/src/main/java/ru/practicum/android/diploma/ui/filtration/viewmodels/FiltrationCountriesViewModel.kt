package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.repository.AreasInteractor
import ru.practicum.android.diploma.ui.utils.XxxLiveData

class FiltrationCountriesViewModel(
    private val countryGetter: AreasInteractor,
) : ViewModel() {

    init {
        viewModelScope.launch {
            getCountries()
        }
    }

    private val _liveData = XxxLiveData<FiltrationCountryData>()

    val liveData: LiveData<FiltrationCountryData> get() = _liveData

    private fun getCountries() {
        viewModelScope.launch {
            val countries = countryGetter.getAllCountries()
            _liveData.postValue(FiltrationCountryData.Countries(countries))
        }
    }
}
