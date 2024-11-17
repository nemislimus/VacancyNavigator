package ru.practicum.android.diploma.domain.search.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyList
import ru.practicum.android.diploma.domain.search.model.SearchVacancyOptions

interface SearchRepository {
    suspend fun searchVacancy(searchOptions: SearchVacancyOptions): Flow<Resource<VacancyList>>
}
