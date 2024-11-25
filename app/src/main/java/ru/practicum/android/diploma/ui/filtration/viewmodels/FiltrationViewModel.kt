package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Geolocation
import ru.practicum.android.diploma.domain.models.SearchFilter
import ru.practicum.android.diploma.domain.repository.GetSearchFilterInteractor
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor
import ru.practicum.android.diploma.ui.utils.XxxLiveData
import ru.practicum.android.diploma.util.BooleanForDetekt

class FiltrationViewModel(
    private val filterSetter: SetSearchFilterInteractor,
    private val filterGetter: GetSearchFilterInteractor,
) : ViewModel(), BooleanForDetekt {
    private val liveData: XxxLiveData<FiltrationData> = XxxLiveData()
    private var isOldFilterLoaded = false
    private var oldFilter = SearchFilter()
    private var lastFilterReceived = SearchFilter()
    private var lastSalarySend: Int? = null

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

                // если отличаются только зарплатой, то не обновляем данные во фрагменте
                if (lastFilterReceived.copy(salary = lastSalarySend) != filter) {
                    liveData.setValue(
                        FiltrationData.Filter(
                            filter = filter
                        )
                    )

                    liveData.setValue(
                        FiltrationData.IsFilterChanged(oldFilter != filter)
                    )
                }

                lastFilterReceived = filter
            }
        }
    }

    fun getLiveData(): LiveData<FiltrationData> = liveData

    fun saveSalary(salary: Int) {
        lastSalarySend = if (salary > 0) salary else null
        viewModelScope.launch {
            filterSetter.saveSalary(
                salary = lastSalarySend
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
