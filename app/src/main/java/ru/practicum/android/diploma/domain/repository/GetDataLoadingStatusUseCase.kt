package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.DataLoadingStatus

class GetDataLoadingStatusUseCase(private val repository: DataLoadingStatusRepository) {
    suspend operator fun invoke(): Flow<DataLoadingStatus> {
        return repository.getStatus()
    }
}
