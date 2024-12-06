package ru.practicum.android.diploma.domain.repository

interface UpdateDbOnAppStartInteractor {
    suspend fun update(): Boolean

    suspend fun setNoInternet()
}
