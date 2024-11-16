package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.Industry

interface IndustriesInteractor {
    suspend fun getAllIndustries(search: String? = null): List<Industry>
}
