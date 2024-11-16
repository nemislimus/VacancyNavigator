package ru.practicum.android.diploma.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.impl.UpdateDbOnAppStartUseCase

class MainActivityViewModel(dataUpdater: UpdateDbOnAppStartUseCase) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataUpdater()
        }
    }
}
