package ru.practicum.android.diploma.data.network.impl

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ru.practicum.android.diploma.data.network.api.NetworkConnectionChecker
import ru.practicum.android.diploma.util.BooleanForDetekt

class NetworkConnectionCheckerImpl(private val connectivityManager: ConnectivityManager) :
    NetworkConnectionChecker,
    BooleanForDetekt {
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
}
