package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.models.FirebaseEvent
import ru.practicum.android.diploma.domain.repository.FirebaseInteractor
import ru.practicum.android.diploma.domain.repository.FirebaseRepository

class FirebaseInteractorImpl(private val repository: FirebaseRepository) : FirebaseInteractor {
    override suspend fun logEvent(event: FirebaseEvent) {
        repository.logEvent(event)
    }
}
