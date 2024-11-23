package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow

interface NetworkConnectionCheckerInteractor {
    fun isConnected(): Boolean

    fun onStateChange(): Flow<Boolean>
}
