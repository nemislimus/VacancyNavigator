package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow

interface NetworkConnectionCheckerRepository {
    fun isConnected(): Boolean

    fun onStateChange(): Flow<Boolean>
}
