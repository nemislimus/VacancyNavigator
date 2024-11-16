package ru.practicum.android.diploma.data.filter.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.XxxDataBase
import ru.practicum.android.diploma.domain.filter.api.FilterRepository
import ru.practicum.android.diploma.domain.models.SearchFilter

class FilterRepositoryImpl(private val dataBase: XxxDataBase) : FilterRepository {
    override suspend fun getActiveFilter(): Flow<SearchFilter?> = flow { emit(null) }
}
