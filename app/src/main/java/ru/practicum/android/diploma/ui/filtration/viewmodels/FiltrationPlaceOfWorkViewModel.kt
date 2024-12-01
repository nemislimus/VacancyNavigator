package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.SearchFilter
import ru.practicum.android.diploma.domain.repository.GetSearchFilterInteractor
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor
import ru.practicum.android.diploma.ui.utils.XxxLiveData

class FiltrationPlaceOfWorkViewModel(
    private val filterGetter: GetSearchFilterInteractor,
    private val filterSetter: SetSearchFilterInteractor
) : ViewModel() {

    private val liveData = XxxLiveData<FiltrationPlaceOfWorkData>()

    private var tempFilter: SearchFilter = SearchFilter()

    init {
        viewModelScope.launch {
            filterSetter.resetAreaTempValue()

            liveData.postValue(
                FiltrationPlaceOfWorkData.OldFilter(
                    filter = filterGetter.getFilter().first() ?: SearchFilter()
                )
            )

            filterGetter.getTempFilter().collect { filter ->
                tempFilter = filter ?: SearchFilter()
                liveData.postValue(
                    FiltrationPlaceOfWorkData.NewFilter(
                        filter = tempFilter
                    )
                )
            }
        }
    }

    fun liveData(): XxxLiveData<FiltrationPlaceOfWorkData> {
        return liveData
    }

    fun setTempArea(area: Area?) {
        viewModelScope.launch {
            filterSetter.saveAreaTempValue(area)
        }
    }

    suspend fun resetTempValue() {
        filterSetter.resetAreaTempValue()
    }

    fun confirmWorkplace() {
        viewModelScope.launch {
            with(tempFilter) {
                if (city != null) {
                    filterSetter.saveArea(city)
                } else if (region != null) {
                    filterSetter.saveArea(region)
                } else {
                    filterSetter.saveArea(country)
                }
            }
            filterSetter.resetAreaTempValue()
            liveData.postValue(FiltrationPlaceOfWorkData.GoBack)
        }
    }
}
