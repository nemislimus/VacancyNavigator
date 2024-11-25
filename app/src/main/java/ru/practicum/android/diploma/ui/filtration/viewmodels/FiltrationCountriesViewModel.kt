package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Area
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
            val countries = setOrder(countryGetter.getAllCountries())
            _liveData.postValue(FiltrationCountryData.Countries(countries))
        }
    }

    private fun setOrder(data: List<Area>): MutableList<Area> {
        val orderedList: MutableList<Area> = MutableList(data.size) { data[0] }
        data.forEach { country ->
            orderedList[setAreaOnPosition(country.name)] = country
        }
        return orderedList
    }

    private fun setAreaOnPosition(name: String): Int {
        return when (name) {
            RUS -> 0
            UKR -> 1
            KAZ -> 2
            AZE -> 2 + 1
            BLR -> 2 + 2
            GEO -> 2 + 2 + 1
            KYR -> 2 + 2 + 2
            UZB -> 2 + 2 + 2 + 1
            else -> 2 + 2 + 2 + 2
        }
    }

    companion object {
        private const val RUS = "Россия"
        private const val UKR = "Украина"
        private const val KAZ = "Казахстан"
        private const val AZE = "Азербайджан"
        private const val BLR = "Беларусь"
        private const val GEO = "Грузия"
        private const val KYR = "Кыргызстан"
        private const val UZB = "Узбекистан"
    }
}
