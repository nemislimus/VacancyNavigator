package ru.practicum.android.diploma.domain.search.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyShort

interface SearchInteractor {
    suspend fun searchVacancy(text: String, page: Int): Flow<Resource<List<VacancyShort>>>
    suspend fun hasActiveFilter(): Flow<Boolean>
}
