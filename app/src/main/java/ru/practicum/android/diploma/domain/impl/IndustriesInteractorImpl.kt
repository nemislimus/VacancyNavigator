package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.repository.IndustriesInteractor
import ru.practicum.android.diploma.domain.repository.IndustriesRepository

class IndustriesInteractorImpl(private val repository: IndustriesRepository) : IndustriesInteractor {
    override suspend fun getAllIndustries(search: String?): List<Industry> {
        return repository.getAllIndustries(search)
    }
}
