package ru.practicum.android.diploma.ui.filtration.viewmodels

import ru.practicum.android.diploma.ui.filtration.model.WorkPlace

sealed class FiltrationPlaceOfWorkState(val workPlace: WorkPlace) {
    class Modified(workPlace: WorkPlace) : FiltrationPlaceOfWorkState(workPlace)
    class Unmodified(workPlace: WorkPlace) : FiltrationPlaceOfWorkState(workPlace)
    class Confirm(workPlace: WorkPlace) : FiltrationPlaceOfWorkState(workPlace)
}
