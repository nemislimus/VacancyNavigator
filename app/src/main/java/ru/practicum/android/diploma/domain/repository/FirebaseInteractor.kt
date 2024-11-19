package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.FirebaseEvent

interface FirebaseInteractor {
    suspend fun logEvent(event: FirebaseEvent)
}
