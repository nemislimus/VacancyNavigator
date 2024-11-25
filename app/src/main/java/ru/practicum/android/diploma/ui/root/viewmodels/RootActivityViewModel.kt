package ru.practicum.android.diploma.ui.root.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.FirebaseEvent
import ru.practicum.android.diploma.domain.repository.FirebaseInteractor

class RootActivityViewModel(private val firebase: FirebaseInteractor) : ViewModel() {
    fun sendVIewScreenEventToStat(screenName: String) {
        viewModelScope.launch {
            firebase.logEvent(
                FirebaseEvent.ViewScreen(
                    name = screenName
                )
            )
        }
    }
}
