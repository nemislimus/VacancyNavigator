package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.repository.NetworkConnectionCheckerInteractor
import ru.practicum.android.diploma.domain.repository.NetworkConnectionCheckerRepository

class NetworkConnectionCheckerInteractorImpl(
    private val repository: NetworkConnectionCheckerRepository
) : NetworkConnectionCheckerInteractor {
    override fun isConnected(): Boolean {
        return repository.isConnected()
    }

    override fun onStateChange(): Flow<Boolean> {
        return repository.onStateChange()
    }
}
