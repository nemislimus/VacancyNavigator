package ru.practicum.android.diploma.ui.favorites.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.repository.FavoriteVacancyInteractor

class FavoritesViewModel(private val favorites: FavoriteVacancyInteractor) : ViewModel() {
    private val liveData = MutableLiveData<FavoritesState>()

    init {
        viewModelScope.launch {
            launch {
                favorites.getList().catch { er ->
                    Log.d("WWW", "$er")
                    liveData.postValue(
                        FavoritesState.DbFail
                    )
                }.collect { list ->
                    liveData.postValue(
                        FavoritesState.Vacancies(list)
                    )
                }
            }
        }
    }

    fun getLiveData(): LiveData<FavoritesState> {
        return liveData
    }
}
