package ru.practicum.android.diploma.ui.vacancy.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.details.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.ui.vacancy.models.VacancyDetailsState

class VacancyViewModel(
    private val vacancyId: String,
    private val context: Context,
    private val interactor: VacancyDetailsInteractor,
) : ViewModel() {
    private var vacancyDetailsStateLiveData =
        MutableLiveData<VacancyDetailsState>()

    fun observeState(): LiveData<VacancyDetailsState> = vacancyDetailsStateLiveData

    init {
        getVacancyDetails(vacancyId)
    }

    private fun getVacancyDetails(id: String) {
        updateState(VacancyDetailsState.Loading)
        viewModelScope.launch {
            interactor.searchVacancyById(id).collect { result ->
                manageDetailsResult(result.first, result.second)
            }
        }
    }

    private fun manageDetailsResult(data: VacancyFull?, message: String?) {
        if (data == null) {
            when (message) {
                Resource.NOT_FOUND -> updateState(
                    VacancyDetailsState.EmptyResult(
                        emptyMessage = context.getString(R.string.vacancy_not_found_or_delete)
                    )
                )
                Resource.CHECK_CONNECTION -> updateState(
                    VacancyDetailsState.NoConnection(
                        errorMessage = context.getString(R.string.no_internet)
                    )
                )
                else -> updateState(
                    VacancyDetailsState.ServerError(
                        errorMessage = context.getString(R.string.server_error)
                    )
                )
            }
        } else {
            updateState(VacancyDetailsState.Content(data))
        }
    }

    private fun updateState(state: VacancyDetailsState) {
        vacancyDetailsStateLiveData.postValue(state)
    }
}
