package ru.practicum.android.diploma.domain.details.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.details.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.search.api.VacancyRepository

class VacancyDetailsInteractorImpl(
    private val vacancyRepository: VacancyRepository,
) : VacancyDetailsInteractor {
    override suspend fun searchVacancyById(id: String): Flow<Resource<VacancyFull>> =
        vacancyRepository.getVacancyDetails(id)
}
