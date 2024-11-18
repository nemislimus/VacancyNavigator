package ru.practicum.android.diploma.domain.search.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.models.VacancyList
import ru.practicum.android.diploma.domain.search.model.SearchVacancyOptions

interface VacancyRepository {
    suspend fun searchVacancy(searchOptions: SearchVacancyOptions): Flow<Resource<VacancyList>>
    suspend fun getVacancyDetails(id: String): Flow<Resource<VacancyFull>>
}
