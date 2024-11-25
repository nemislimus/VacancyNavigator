package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Geolocation
import ru.practicum.android.diploma.domain.models.SearchFilter
import ru.practicum.android.diploma.domain.repository.GetSearchFilterInteractor
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor
import ru.practicum.android.diploma.ui.utils.XxxLiveData

class FiltrationViewModel(
    private val filterSetter: SetSearchFilterInteractor,
    private val filterGetter: GetSearchFilterInteractor,
) : ViewModel() {
    private val liveData: XxxLiveData<FiltrationData> = XxxLiveData()
    private var isOldFilterLoaded = false
    private var oldFilter: SearchFilter? = null

    init {
        viewModelScope.launch {
            filterGetter.getFilter().collect { filter ->
                if (!isOldFilterLoaded) {
                    // c ним будем сравнивать, чтобы понимать надо показывать "применить" / "сбросить"
                    oldFilter = filter
                }

                liveData.setValue(
                    FiltrationData.Filter(
                        filter = filter
                    )
                )

                liveData.setValue(
                    FiltrationData.IsFilterChanged(oldFilter != filter)
                )
            }
        }
    }

    fun getLiveData(): LiveData<FiltrationData> = liveData

    fun onSalaryInput

    fun saveSalary(salary: Int) {
        viewModelScope.launch {
            filterSetter.saveSalary(
                salary = if (salary > 0) salary else null
            )
        }
    }

    fun setOnlyWithSalary(withSalary: Boolean) {
        viewModelScope.launch {
            filterSetter.saveOnlyWithSalary(
                onlyWithSalary = withSalary
            )
        }
    }

    fun setGeolocation(geolocation: Geolocation?) {
        viewModelScope.launch {
            filterSetter.saveGeolocation(
                geolocation = geolocation
            )
        }
    }

    fun resetFilter() {
        viewModelScope.launch {
            filterSetter.resetFilter()
        }
    }

    fun goBack(applyBeforeExiting: Boolean) {
        viewModelScope.launch {
            liveData.setSingleEventValue(
                FiltrationData.GoBack(applyBeforeExiting)
            )
        }
    }
}
