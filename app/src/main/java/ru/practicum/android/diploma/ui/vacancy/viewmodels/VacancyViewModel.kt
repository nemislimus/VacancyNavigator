package ru.practicum.android.diploma.ui.vacancy.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.vacancy.models.VacancyDetailsState

class VacancyViewModel(
    private val vacancyId: String,
    private val context: Context,
) : ViewModel() {
    private var vacancyDetailsStateLiveData =
        MutableLiveData<VacancyDetailsState>()

    fun observeState(): LiveData<VacancyDetailsState> = vacancyDetailsStateLiveData

    init {
        updateState(VacancyDetailsState.Loading)
        getVacancyDetails()
    }

    private fun getVacancyDetails() {
        updateState(VacancyDetailsState.EmptyResult(context.getString(R.string.vacancy_not_found_or_delete)))
        updateState(VacancyDetailsState.ServerError(context.getString(R.string.server_error)))
    }

    private fun updateState(state: VacancyDetailsState) {
        vacancyDetailsStateLiveData.postValue(state)
    }
}
