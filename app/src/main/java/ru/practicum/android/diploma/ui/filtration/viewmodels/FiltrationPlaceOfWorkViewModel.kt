package ru.practicum.android.diploma.ui.filtration.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filtration.api.FiltrationPlaceOfWorkInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor
import ru.practicum.android.diploma.ui.filtration.model.WorkPlace
import ru.practicum.android.diploma.ui.utils.StateViewModel

class FiltrationPlaceOfWorkViewModel(
    private val interactor: FiltrationPlaceOfWorkInteractor,
    private val filterSetter: SetSearchFilterInteractor,
    private val previousArea: Area?
) :
    StateViewModel<FiltrationPlaceOfWorkState>() {

    private var currentCountry: Area? = null
    private var currentRegion: Area? = null

    private var initCountryJob: Job? = null

    init {
        previousArea?.let { area ->
            viewModelScope.launch {
                interactor.getCountryByRegionId(area.id).collect { country ->
                    country?.let {
                        currentCountry = country
                        if (country != area) {
                            currentRegion = area
                        }
                    } ?: let { currentCountry = area }
                    renderContent()
                }
            }
        }
    }

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
        viewModelScope.launch(Dispatchers.Main) {
            currentRegion?.let {
                filterSetter.saveArea(it)
            } ?: run {
                filterSetter.saveArea(currentCountry)
            }
            renderState(FiltrationPlaceOfWorkState.Confirm(currentWorkPlace()))
        }
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
        val modify = when (previousArea) {
            null -> currentCountry != null || currentRegion != null
            else -> currentRegion?.let { previousArea.id != it.id }
                ?: let { currentCountry?.let { previousArea.id != it.id } } ?: true
        }

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
