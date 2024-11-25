package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filtration.api.FiltrationPlaceOfWorkInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.filtration.model.WorkPlace
import ru.practicum.android.diploma.ui.utils.StateViewModel

class FiltrationPlaceOfWorkViewModel(private val interactor: FiltrationPlaceOfWorkInteractor) :
    StateViewModel<FiltrationPlaceOfWorkState>() {

    private val previousArea: Area? = null

    private var currentCountry: Area? = null
    private var currentRegion: Area? = null

    private var initCountryJob: Job? = null

    fun getCurrentCountry() = currentCountry

    fun countryChange(country: Area?) {
        currentCountry = country
        currentCountry?.let { currentRegion = null }
        renderContent()
    }

    fun regionChange(region: Area?) {
        currentRegion = region
        currentCountry ?: initCountryByRegion()
        renderContent()
    }

    fun confirmWorkplace() {
        renderState(FiltrationPlaceOfWorkState.Confirm(currentWorkPlace()))
    }

    private fun initCountryByRegion() {
        initCountryJob?.cancel()
        currentRegion?.let { region ->
            initCountryJob = viewModelScope.launch {
                interactor.getCountryByRegionId(regionId = region.id).collect {
                    currentCountry = it
                    renderContent()
                }
            }
        }
    }

    private fun renderContent() {
        val modify = previousArea?.id != currentRegion?.id
        val workPlace = currentWorkPlace()
        renderState(
            if (modify) {
                FiltrationPlaceOfWorkState.Modified(workPlace)
            } else {
                FiltrationPlaceOfWorkState.Unmodified(workPlace)
            }
        )
    }

    private fun currentWorkPlace(): WorkPlace = WorkPlace(currentCountry, currentRegion)

}
