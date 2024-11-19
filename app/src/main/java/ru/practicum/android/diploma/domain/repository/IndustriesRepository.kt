package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.Industry

interface IndustriesRepository {
    suspend fun getAllIndustries(search: String? = null): List<Industry>
}
