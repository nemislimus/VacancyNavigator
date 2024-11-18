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
    private var context: Context?,
    private val interactor: VacancyDetailsInteractor,
) : ViewModel() {
    private var vacancyDetailsStateLiveData =
        MutableLiveData<VacancyDetailsState>()

    fun observeState(): LiveData<VacancyDetailsState> = vacancyDetailsStateLiveData

    init {
        getVacancyDetails(vacancyId)
    }

    override fun onCleared() {
        context = null
        super.onCleared()
    }

    private fun getVacancyDetails(id: String) {
        updateState(VacancyDetailsState.Loading)
        viewModelScope.launch {
            interactor.searchVacancyById(id).collect { result ->
                manageDetailsResult(result)
            }
        }
    }

    private fun manageDetailsResult(result: Resource<VacancyFull>) {
        when (result) {
            is Resource.ConnectionError -> context?.let {
                VacancyDetailsState.NoConnection(
                    errorMessage = it.getString(R.string.no_internet)
                )
            }?.let {
                updateState(
                    it
                )
            }

            is Resource.NotFoundError -> context?.let {
                VacancyDetailsState.EmptyResult(
                    emptyMessage = it.getString(R.string.vacancy_not_found_or_delete)
                )
            }?.let {
                updateState(
                    it
                )
            }

            is Resource.ServerError -> context?.getString(R.string.server_error)?.let {
                VacancyDetailsState.ServerError(
                    errorMessage = it
                )
            }?.let {
                updateState(
                    it
                )
            }

            is Resource.Success -> updateState(VacancyDetailsState.Content(result.data))
        }
    }

    private fun updateState(state: VacancyDetailsState) {
        vacancyDetailsStateLiveData.postValue(state)
    }
}
