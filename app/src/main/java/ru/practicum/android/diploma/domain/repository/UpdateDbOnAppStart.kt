package ru.practicum.android.diploma.domain.repository

interface UpdateDbOnAppStart {
    suspend fun update(): Boolean
}
