package ru.practicum.android.diploma.ui.vacancy.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.details.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.repository.FavoriteVacancyInteractor
import ru.practicum.android.diploma.ui.vacancy.models.VacancyDetailsState

class VacancyViewModel(
    private val vacancyId: String,
    private var context: Context?,
    private val interactor: VacancyDetailsInteractor,
    private val favInteractor: FavoriteVacancyInteractor,
) : ViewModel() {

    private var currentVacancy: VacancyFull? = null
    var vacancyIsFavorite = false

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
            is Resource.ConnectionError -> context?.getString(R.string.no_internet)?.let {
                VacancyDetailsState.NoConnection(errorMessage = it)
            }?.let { updateState(it) }

            is Resource.NotFoundError -> context?.getString(R.string.vacancy_not_found_or_delete)?.let {
                VacancyDetailsState.EmptyResult(emptyMessage = it)
            }?.let { updateState(it) }

            is Resource.ServerError -> context?.getString(R.string.server_error)?.let {
                VacancyDetailsState.ServerError(errorMessage = it)
            }?.let { updateState(it) }

            is Resource.Success -> {
                viewModelScope.launch {
                    checkFavorite()
                    currentVacancy = result.data
                    updateState(VacancyDetailsState.Content(result.data))
                }
            }
        }
    }

    private fun updateState(state: VacancyDetailsState) {
        vacancyDetailsStateLiveData.postValue(state)
    }

    private suspend fun checkFavorite() {
        vacancyIsFavorite = favInteractor.getById(vacancyId) != null
    }

    suspend fun clickOnFavoriteIcon(state: VacancyDetailsState?) {
        if (state is VacancyDetailsState.Content) {
            if (vacancyIsFavorite) removeVacancyFromFavorite() else addVacancyToFavorite()
        } else {
            showDeny()
        }
    }

    private suspend fun addVacancyToFavorite() {
        currentVacancy?.let { favInteractor.add(it) }
        vacancyIsFavorite = true
    }

    private suspend fun removeVacancyFromFavorite() {
        currentVacancy?.let { favInteractor.remove(it.id) }
        vacancyIsFavorite = false
    }

    private fun showDeny() {
        Toast.makeText(context, context?.getString(R.string.cant_add_to_favorite), Toast.LENGTH_SHORT)
            .show()
    }
}
