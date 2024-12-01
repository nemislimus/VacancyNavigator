package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.SearchFilter
import ru.practicum.android.diploma.domain.repository.GetSearchFilterInteractor
import ru.practicum.android.diploma.domain.repository.GetSearchFilterRepository

class GetSearchFilterInteractorImpl(private val repository: GetSearchFilterRepository) : GetSearchFilterInteractor {
    override suspend fun getFilter(): Flow<SearchFilter?> {
        return repository.getFilter()
    }

    override suspend fun isFilterExists(): Flow<Boolean> {
        return repository.isFilterExists()
    }

    override suspend fun getFilterForNetworkClient(page: Int): SearchFilter? {
        return repository.getFilterForNetworkClient(page)
    }

    override suspend fun getTempFilter(): Flow<SearchFilter?> {
        return repository.getTempFilter()
    }
}
