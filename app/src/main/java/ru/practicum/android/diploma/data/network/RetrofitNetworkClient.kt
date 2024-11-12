package ru.practicum.android.diploma.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.network.api.HhSearchApi
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.mapper.NetworkMapper
import ru.practicum.android.diploma.data.search.dto.request.AreaRequest
import ru.practicum.android.diploma.data.search.dto.request.CountryRequest
import ru.practicum.android.diploma.data.search.dto.request.IndustryRequest
import ru.practicum.android.diploma.data.search.dto.request.VacancyDetailedRequest
import ru.practicum.android.diploma.data.search.dto.request.VacancyRequest
import ru.practicum.android.diploma.data.search.dto.response.AreaResponse
import ru.practicum.android.diploma.data.search.dto.response.CountryResponse
import ru.practicum.android.diploma.data.search.dto.response.IndustryResponse
import ru.practicum.android.diploma.data.search.dto.response.VacancyDetailedResponse
import ru.practicum.android.diploma.data.search.dto.response.VacancyResponse

class RetrofitNetworkClient(
    private val hhSearchApi: HhSearchApi,
    private val connectivityManager: ConnectivityManager,
    private val mapper: NetworkMapper,
) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        when {
            !isConnected() -> return Response().apply { resultCode = NO_CONNECTION_CODE }
            dto !is AreaRequest
                    && dto !is CountryRequest
                    && dto !is IndustryRequest
                    && dto !is VacancyDetailedRequest
                    && dto !is VacancyRequest -> return Response().apply {
                resultCode = INCORRECT_PARAM_ERROR_CODE
            }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is CountryRequest -> CountryResponse(
                        hhSearchApi.getCountries()
                    )

                    is AreaRequest -> AreaResponse(
                        hhSearchApi.getAreasByCountry(dto.countryId)
                    )

                    is IndustryRequest -> IndustryResponse(
                        hhSearchApi.getIndustries()
                    )

                    is VacancyRequest -> VacancyResponse(
                        hhSearchApi.searchVacancies(
                            mapper.map(dto.options)
                        )
                    )

                    else -> VacancyDetailedResponse(
                        hhSearchApi.getVacancyDetails(
                            (dto as VacancyDetailedRequest).vacancyId
                        )
                    )
                }
                response.apply { resultCode = SUCCESSFUL_RESPONSE_CODE }
            } catch (e: HttpException) {
                Log.d("REQUEST_EXCEPTION", e.message())
                e.code()
                badResponse()
            }

        }
    }

    private fun badResponse() = Response().apply { resultCode = INTERNAL_SERV_ERROR_CODE }

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
        const val SUCCESSFUL_RESPONSE_CODE = 200
        const val INCORRECT_PARAM_ERROR_CODE = 400
        const val CAPTCHA_REQUIRED_ERROR = 403
        const val NOT_FOUND_CODE = 404
        const val INTERNAL_SERV_ERROR_CODE = 500
        const val BAD_GATEWAY_CODE = 502
    }

}
