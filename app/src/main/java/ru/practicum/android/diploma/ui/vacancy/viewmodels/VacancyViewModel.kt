package ru.practicum.android.diploma.ui.vacancy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.details.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.repository.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.repository.SystemInteractor
import ru.practicum.android.diploma.domain.sharing.api.SharingInteractor
import ru.practicum.android.diploma.ui.vacancy.models.VacancyDetailsState

class VacancyViewModel(
    private val vacancyId: String,
    private var android: SystemInteractor,
    private val vacancyInteractor: VacancyDetailsInteractor,
    private val favoriteInteractor: FavoriteVacancyInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {
    @Volatile
    private var currentVacancy: VacancyFull? = null

    @Volatile
    private var vacancyIsFavorite = false

    private val vacancyDetailsStateLiveData =
        MutableLiveData<VacancyDetailsState>()

    private val isFavoriteLiveData = MutableLiveData<Boolean>()

    fun observeState(): LiveData<VacancyDetailsState> = vacancyDetailsStateLiveData

    fun observeIsFavorite(): LiveData<Boolean> = isFavoriteLiveData

    init {
        getVacancyDetails(vacancyId)
    }

    private fun getVacancyDetails(id: String) {
        viewModelScope.launch {
            updateState(VacancyDetailsState.Loading)

            favoriteInteractor.getById(id)?.let { vacancy ->
                vacancyIsFavorite = true
                currentVacancy = vacancy
                updateState(VacancyDetailsState.Content(vacancy))
            }

            launch {
                vacancyInteractor.searchVacancyById(id).collect { result ->
                    when (result) {
                        is Resource.ConnectionError -> {
                            if (currentVacancy == null) {
                                manageDetailsResult(result)
                            }
                        }

                        is Resource.NotFoundError -> {
                            // прокидываем результат, отрабатываем удаление вакансии
                            currentVacancy = null
                            vacancyIsFavorite = false
                            manageDetailsResult(result)
                        }

                        is Resource.ServerError -> {
                            if (currentVacancy == null) {
                                manageDetailsResult(result)
                            }
                        }

                        is Resource.Success -> {
                            // если вакансия еще не на странице или отличается от той, что на странице обновляем ее
                            if (currentVacancy != result.data) {
                                currentVacancy = result.data
                                manageDetailsResult(result)

                                if (vacancyIsFavorite) {
                                    // обновляем вакансию в БД
                                    favoriteInteractor.add(result.data)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun manageDetailsResult(result: Resource<VacancyFull>) {
        when (result) {
            is Resource.ConnectionError -> android.getString(R.string.no_internet)?.let {
                VacancyDetailsState.NoConnection(errorMessage = it)
            }?.let { updateState(it) }

            is Resource.NotFoundError -> android.getString(R.string.vacancy_not_found_or_delete)?.let {
                VacancyDetailsState.EmptyResult(emptyMessage = it)
            }?.let { updateState(it) }

            is Resource.ServerError -> android.getString(R.string.server_error)?.let {
                VacancyDetailsState.ServerError(errorMessage = it)
            }?.let { updateState(it) }

            is Resource.Success -> {
                updateState(VacancyDetailsState.Content(result.data))
            }
        }
    }

    private suspend fun updateState(state: VacancyDetailsState) {
        withContext(Dispatchers.Main) {
            vacancyDetailsStateLiveData.value = state
            isFavoriteLiveData.value = vacancyIsFavorite
        }
    }

    fun clickOnFavoriteIcon(state: VacancyDetailsState?) {
        viewModelScope.launch {
            if (state is VacancyDetailsState.Content) {
                currentVacancy?.let {
                    vacancyIsFavorite = !vacancyIsFavorite
                    if (vacancyIsFavorite) {
                        favoriteInteractor.add(it)
                    } else {
                        favoriteInteractor.remove(it.id)
                    }
                }

                isFavoriteLiveData.postValue(vacancyIsFavorite)
            } else {
                withContext(Dispatchers.Main) {
                    showDeny(FAV_TOAST_MARKER)
                }
            }
        }
    }

    private fun showDeny(marker: Int) {
        when (marker) {
            FAV_TOAST_MARKER -> android.showToast(
                android.getString(R.string.cant_add_to_favorite)
            )

            else -> android.showToast(
                android.getString(R.string.cant_share)
            )
        }
    }

    fun clickOnShareIcon(state: VacancyDetailsState?) {
        if (state is VacancyDetailsState.Content) {
            currentVacancy?.let { sharingInteractor.shareAppMessageOrLink(it.urlHh) }
        } else {
            showDeny(SHARE_TOAST_MARKER)
        }
    }

    fun removeCurrentVacancy() {
        viewModelScope.launch {
            favoriteInteractor.remove(vacancyId)
        }
    }

    companion object {
        private const val FAV_TOAST_MARKER = 0
        private const val SHARE_TOAST_MARKER = 1
    }
}
