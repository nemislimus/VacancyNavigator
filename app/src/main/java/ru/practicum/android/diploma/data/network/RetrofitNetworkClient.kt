package ru.practicum.android.diploma.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ru.practicum.android.diploma.data.network.api.HhSearchApi
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.mapper.NetworkMapper
import ru.practicum.android.diploma.data.search.dto.request.AreaRequest
import ru.practicum.android.diploma.data.search.dto.request.CountryRequest
import ru.practicum.android.diploma.data.search.dto.request.IndustryRequest
import ru.practicum.android.diploma.data.search.dto.request.VacancyDetailedRequest
import ru.practicum.android.diploma.data.search.dto.request.VacancyRequest

class RetrofitNetworkClient(
    private val hhSearchApi: HhSearchApi,
    private val connectivityManager: ConnectivityManager,
    private val mapper: NetworkMapper,
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = NO_CONNECTION_CODE }
        }

        if (dto !is AreaRequest
            && dto !is CountryRequest
            && dto !is IndustryRequest
            && dto !is VacancyDetailedRequest
            && dto !is VacancyRequest
        ) {
            return Response().apply { resultCode = BAD_REQUEST_CODE }
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
        const val BAD_REQUEST_CODE = 400
        const val NOT_FOUND_CODE = 404
    }

}
