package ru.practicum.android.diploma.data.search.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.domain.search.api.SearchRepository
import ru.practicum.android.diploma.domain.search.model.SearchVacancyOptions

class SearchRepositoryImpl(private val networkClient: NetworkClient) : SearchRepository {
    override suspend fun searchVacancy(searchOptions: SearchVacancyOptions): Flow<List<VacancyShort>> = flow {}
}
