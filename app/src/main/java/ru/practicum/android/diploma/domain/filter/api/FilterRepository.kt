package ru.practicum.android.diploma.domain.filter.api

import ru.practicum.android.diploma.domain.models.SearchFilter

interface FilterRepository {
    suspend fun getActiveFilter(): SearchFilter?
}
