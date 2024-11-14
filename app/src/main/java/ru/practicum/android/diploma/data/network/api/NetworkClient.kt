package ru.practicum.android.diploma.data.network.api

import ru.practicum.android.diploma.data.network.ApiRequest
import ru.practicum.android.diploma.data.network.ApiResponse

interface NetworkClient {
    suspend fun doRequest(dto: ApiRequest): ApiResponse
}
