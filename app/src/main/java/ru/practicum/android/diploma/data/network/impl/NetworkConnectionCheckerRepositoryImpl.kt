package ru.practicum.android.diploma.data.network.impl

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.repository.NetworkConnectionCheckerRepository
import ru.practicum.android.diploma.util.BooleanForDetekt

class NetworkConnectionCheckerRepositoryImpl(
    private val connectivityManager: ConnectivityManager,
    private val scope: CoroutineScope,
    private val networkStateFlow: MutableStateFlow<Boolean>
) :
    NetworkConnectionCheckerRepository,
    BooleanForDetekt {
    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // network is available for use
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                scope.launch {
                    delay(DELAY_BEFORE_EMIT)
                    if (isConnected()) {
                        networkStateFlow.emit(true)
                    }
                }
            }

            // lost network connection
            override fun onLost(network: Network) {
                super.onLost(network)
                scope.launch {
                    delay(DELAY_BEFORE_EMIT)
                    if (!isConnected()) {
                        networkStateFlow.emit(false)
                    }
                }
            }
        }

        connectivityManager.requestNetwork(networkRequest, networkCallback)

        scope.launch {
            networkStateFlow.emit(isConnected())
        }
    }

    override fun onStateChange(): Flow<Boolean> {
        return getFlow()
    }

    override fun isConnected(): Boolean {
        var result = false

        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
            result = ifOneTrue(
                it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR),
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI),
                it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            )
        }

        return result
    }

    private fun getFlow(): StateFlow<Boolean> {
        return networkStateFlow
    }

    companion object {
        private const val DELAY_BEFORE_EMIT = 200L
    }
}
