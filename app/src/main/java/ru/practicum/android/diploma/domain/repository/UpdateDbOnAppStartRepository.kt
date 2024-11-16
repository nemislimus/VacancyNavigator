package ru.practicum.android.diploma.domain.repository

interface UpdateDbOnAppStartRepository {
    suspend fun update(): Boolean
}
