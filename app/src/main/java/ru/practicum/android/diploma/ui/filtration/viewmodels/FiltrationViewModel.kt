package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Geolocation
import ru.practicum.android.diploma.domain.models.SearchFilter
import ru.practicum.android.diploma.domain.repository.GetSearchFilterInteractor
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor
import ru.practicum.android.diploma.domain.repository.SystemInteractor
import ru.practicum.android.diploma.ui.utils.XxxLiveData
import ru.practicum.android.diploma.util.NumDeclension

class FiltrationViewModel(
    private val filterSetter: SetSearchFilterInteractor,
    private val filterGetter: GetSearchFilterInteractor,
    private val systemInt: SystemInteractor
) : ViewModel(), NumDeclension {
    private val liveData: XxxLiveData<FiltrationData> = XxxLiveData()
    private var isOldFilterLoaded = false
    private var oldFilter = SearchFilter()
    private var lastFilterReceived = SearchFilter()
    private val defaultFilter = SearchFilter()
    private var isReset = true
    private var job: Job? = null
    private var salary: Int? = null

    init {
        viewModelScope.launch {
            filterGetter.getFilter().map {
                it ?: SearchFilter()
            }.collect { filter ->
                if (!isOldFilterLoaded) {
                    // c ним будем сравнивать, чтобы понимать надо показывать "применить" / "сбросить"
                    oldFilter = filter
                    isOldFilterLoaded = true
                }

                salary = filter.salary

                // если отличаются только зарплатой, то не обновляем данные во фрагменте
                if (isReset || lastFilterReceived.copy(salary = 0) != filter.copy(salary = 0)) {
                    liveData.setValue(
                        FiltrationData.Filter(
                            filter = filter
                        )
                    )
                    isReset = false
                } else {
                    liveData.setStartValue(
                        FiltrationData.Filter(
                            filter = filter
                        )
                    )
                }

                if (lastFilterReceived != filter) {
                    liveData.setValue(
                        FiltrationData.ApplyButton(oldFilter != filter)
                    )
                    liveData.setValue(
                        FiltrationData.ResetButton(defaultFilter != filter)
                    )
                }
                lastFilterReceived = filter
            }
        }
    }

    fun getLiveData(): LiveData<FiltrationData> = liveData

    fun saveSalary(salary: Int?) {
        val salaryValue = if (salary != null && salary > 0) salary else null
        if (this.salary == salaryValue) return
        viewModelScope.launch {
            filterSetter.saveSalary(
                salary = salaryValue
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
        isReset = true
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

    fun showHighSalaryInfo(salary: Long) {
        if (job == null) {
            job = viewModelScope.launch(Dispatchers.Main) {
                systemInt.showToast(
                    systemInt.getString(R.string.salary_to_high)
                        .replace(
                            "%s",
                            threeZeroFormat(salary = salary.toString()) + " " + systemInt.getString(R.string.rub)
                        )
                )
                delay(DEBOUNCE_DELAY)
                job = null
            }
        }
    }

    fun resetWorkplace() {
        viewModelScope.launch {
            filterSetter.saveArea(null)
        }
    }

    fun resetIndustry() {
        viewModelScope.launch {
            filterSetter.saveIndustry(null)
        }
    }

    companion object {
        const val DEBOUNCE_DELAY = 3000L
    }
}
