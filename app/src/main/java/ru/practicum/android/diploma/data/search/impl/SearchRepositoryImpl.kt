package ru.practicum.android.diploma.data.search.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.network.ApiRequest
import ru.practicum.android.diploma.data.network.ApiResponse
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.mapper.NetworkMapper
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyList
import ru.practicum.android.diploma.domain.search.api.SearchRepository
import ru.practicum.android.diploma.domain.search.model.SearchVacancyOptions

class SearchRepositoryImpl(private val networkClient: NetworkClient, private val mapper: NetworkMapper) :
    SearchRepository {
    override suspend fun searchVacancy(searchOptions: SearchVacancyOptions): Flow<Resource<VacancyList>> =
        flow {
            val request = ApiRequest.Vacancy(searchOptions = searchOptions)
            val response = networkClient.doRequest(request)
            emit(
                when (response.resultCode) {
                    ApiResponse.SUCCESSFUL_RESPONSE_CODE -> {
                        val vacancies = (response as ApiResponse.VacancyResponse)
                        Resource.Success(mapper.map(vacancies))
                    }

                    ApiResponse.NO_CONNECTION_CODE -> {
                        Resource.ConnectionError("check connection")
                    }

                    ApiResponse.NOT_FOUND_CODE -> Resource.NotFoundError("404 - not founded")
                    else -> Resource.ServerError("error code - ${response.resultCode}")
                }
            )
        }.flowOn(Dispatchers.IO)
}
