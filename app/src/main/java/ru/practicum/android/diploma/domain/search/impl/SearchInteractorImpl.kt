package ru.practicum.android.diploma.domain.search.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyList
import ru.practicum.android.diploma.domain.repository.GetSearchFilterRepository
import ru.practicum.android.diploma.domain.search.api.SearchInteractor
import ru.practicum.android.diploma.domain.search.api.SearchRepository
import ru.practicum.android.diploma.domain.search.model.SearchVacancyOptions

class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
    private val filterRepository: GetSearchFilterRepository
) : SearchInteractor {
    override suspend fun searchVacancy(text: String, page: Int): Flow<Resource<VacancyList>> {
        val filter = filterRepository.getFilterForNetworkClient(page)
        val options = SearchVacancyOptions(
            text = text,
            page = page,
            filter = filter
        )
        return searchRepository.searchVacancy(options)
    }

    override suspend fun hasActiveFilter(): Flow<Boolean> = filterRepository.isFilterExists()
}
