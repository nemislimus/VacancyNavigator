package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.DataLoadingStatus

interface DataLoadingStatusRepository {
    suspend fun getStatus(): Flow<DataLoadingStatus>

    suspend fun setStatus(status: DataLoadingStatus)
}
