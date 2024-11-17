package ru.practicum.android.diploma.domain.search.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyList

interface SearchInteractor {
    suspend fun searchVacancy(text: String, page: Int): Flow<Resource<VacancyList>>
    suspend fun hasActiveFilter(): Flow<Boolean>
}
