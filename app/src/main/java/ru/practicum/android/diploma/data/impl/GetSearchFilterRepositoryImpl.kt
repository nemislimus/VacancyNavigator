package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.converters.SearchFilterToSearchFilterRoomMapper
import ru.practicum.android.diploma.data.db.dao.SearchFilterDao
import ru.practicum.android.diploma.domain.models.SearchFilter
import ru.practicum.android.diploma.domain.repository.GetSearchFilterRepository

class GetSearchFilterRepositoryImpl(
    private val dao: SearchFilterDao
) : GetSearchFilterRepository {
    private var activeFilter: SearchFilter? = null
    private var hasActiveFilter: Boolean = false
    private val mapper = SearchFilterToSearchFilterRoomMapper

    override suspend fun getFilter(): Flow<SearchFilter?> {
        return dao.getFilterFlow().map { mapper.map(it) }
    }

    override suspend fun isFilterExists(): Flow<Boolean> {
        return dao.getFilterFlow().map { it != null }
    }

    override suspend fun getFilterForNetworkClient(page: Int): SearchFilter? {
        if (hasActiveFilter && page > 0) {
            return activeFilter
        }

        activeFilter = mapper.map(
            filter = dao.getFilter()
        )

        hasActiveFilter = true

        return activeFilter
    }

    override suspend fun getTempFilter(): Flow<SearchFilter?> {
        return dao.getTempFilterFlow().map { mapper.map(it) }
    }
}
