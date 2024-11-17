package ru.practicum.android.diploma.domain.search.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.domain.repository.SearchFilterRepository
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.domain.search.api.SearchInteractor
import ru.practicum.android.diploma.domain.search.api.SearchRepository

class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
    private val filterRepository: SearchFilterRepository
) : SearchInteractor {
    override suspend fun searchVacancy(text: String, page: Int): Flow<Result<List<VacancyShort>>> = flow {}

    override suspend fun hasActiveFilter(): Flow<Boolean> = filterRepository.isFilterExists()
}
