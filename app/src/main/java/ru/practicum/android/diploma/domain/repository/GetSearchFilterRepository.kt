package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Geolocation
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.SearchFilter

interface GetSearchFilterRepository {
    suspend fun getFilter(): Flow<SearchFilter?>

    suspend fun isFilterExists(): Flow<Boolean>

    suspend fun getFilterForNetworkClient(page: Int): SearchFilter?
}
