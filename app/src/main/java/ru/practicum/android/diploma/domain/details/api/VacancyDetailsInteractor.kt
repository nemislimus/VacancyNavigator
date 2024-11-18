package ru.practicum.android.diploma.domain.details.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyFull

interface VacancyDetailsInteractor {
    suspend fun searchVacancyById(id: String): Flow<Resource<VacancyFull>>
}
