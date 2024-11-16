package ru.practicum.android.diploma.domain.filter.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.SearchFilter

interface FilterRepository {
    suspend fun getActiveFilter(): Flow<SearchFilter?>
    suspend fun activeFilterExist(): Flow<Boolean>
}
