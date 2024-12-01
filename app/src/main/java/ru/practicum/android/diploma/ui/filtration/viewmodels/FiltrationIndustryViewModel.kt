package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.repository.GetSearchFilterInteractor
import ru.practicum.android.diploma.domain.repository.IndustriesInteractor
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor
import ru.practicum.android.diploma.ui.utils.XxxLiveData

class FiltrationIndustryViewModel(
    private val industriesGetter: IndustriesInteractor,
    private val filterSetter: SetSearchFilterInteractor,
    private val filterGetter: GetSearchFilterInteractor
) : ViewModel() {
    private val liveData = XxxLiveData<FiltrationIndustryData>()
    private var selectedIndustry: Industry? = null

    init {
        viewModelScope.launch {
            filterGetter.getFilter().first().let { filter ->
                selectedIndustry = filter?.industry
            }

            getIndustries()
        }
    }

    fun getLiveData(): LiveData<FiltrationIndustryData> = liveData

    fun getIndustries(search: String? = null) {
        viewModelScope.launch {
            industriesGetter.getAllIndustries(search).toMutableList().let { industries ->
                selectedIndustry?.let {
                    industries.forEachIndexed { index, industry ->
                        if (selectedIndustry == industry) {
                            industries[index] = industry.copy(isSelected = true)
                        }
                    }
                }

                liveData.postValue(FiltrationIndustryData.Industries(industries))
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
