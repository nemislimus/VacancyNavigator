package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.FirebaseEvent

interface FirebaseRepository {
    suspend fun logEvent(event: FirebaseEvent)
}
