package ru.practicum.android.diploma.domain.search.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyShort

interface SearchRepository {
    suspend fun searchVacancy(text: String, page: Int): Flow<List<VacancyShort>>
    suspend fun hasActiveFilter(): Flow<Boolean>
}
