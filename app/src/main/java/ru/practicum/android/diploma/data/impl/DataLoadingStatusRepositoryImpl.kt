package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.converters.DataLoadingStatusToDataLoadingStatusRoomMapper
import ru.practicum.android.diploma.data.db.dao.DataLoadingStatusDao
import ru.practicum.android.diploma.domain.models.DataLoadingStatus
import ru.practicum.android.diploma.domain.repository.DataLoadingStatusRepository

class DataLoadingStatusRepositoryImpl(private val dao: DataLoadingStatusDao) : DataLoadingStatusRepository {
    override suspend fun getStatus(): Flow<DataLoadingStatus> {
        return dao.getStatus().map { DataLoadingStatusToDataLoadingStatusRoomMapper.map(it) }
    }

    override suspend fun setStatus(status: DataLoadingStatus) {
        dao.insert(DataLoadingStatusToDataLoadingStatusRoomMapper.map(status))
    }
}
