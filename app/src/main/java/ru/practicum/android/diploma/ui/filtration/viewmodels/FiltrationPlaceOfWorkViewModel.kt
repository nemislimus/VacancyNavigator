package ru.practicum.android.diploma.ui.filtration.viewmodels

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.filtration.model.WorkPlace
import ru.practicum.android.diploma.ui.utils.StateViewModel

class FiltrationPlaceOfWorkViewModel : StateViewModel<FiltrationPlaceOfWorkState>() {

    private val previousArea: Area? = null

    private var currentCountry: Area? = null
    private var currentRegion: Area? = null

    fun countryChange(country: Area?) {
        currentCountry = country
        currentCountry?.let { currentRegion = null }
        renderContent()
    }

    fun regionChange(region: Area?) {
        currentRegion = region
        renderContent()
    }

    fun confirmWorkplace() {
        renderState(FiltrationPlaceOfWorkState.Confirm(currentWorkPlace()))
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
