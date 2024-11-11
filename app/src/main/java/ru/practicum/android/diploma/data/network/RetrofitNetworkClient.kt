package ru.practicum.android.diploma.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ru.practicum.android.diploma.data.network.api.HhSearchApi
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.mapper.NetworkMapper

class RetrofitNetworkClient(
    private val hhSearchApi: HhSearchApi,
    private val connectivityManager: ConnectivityManager,
    private val mapper: NetworkMapper,
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = NO_CONNECTION_CODE }
        }
        return Response().apply { resultCode = NOT_FOUND_CODE }
    }

    private fun isConnected(): Boolean {
        var result = false

        connectivityManager
            .getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.let {
                result = it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            }

        return result
    }

    companion object {
        const val NO_CONNECTION_CODE = -1
        const val NOT_FOUND_CODE = -1
    }

}
