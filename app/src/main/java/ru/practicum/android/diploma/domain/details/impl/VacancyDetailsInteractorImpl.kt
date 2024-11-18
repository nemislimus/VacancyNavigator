package ru.practicum.android.diploma.domain.details.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.details.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.models.Resource
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.search.api.VacancyRepository

class VacancyDetailsInteractorImpl(
    private val vacancyRepository: VacancyRepository,
) : VacancyDetailsInteractor {
    override suspend fun searchVacancyById(id: String): Flow<Pair<VacancyFull?, String?>> {
        return vacancyRepository.getVacancyDetails(id).map { resource ->
            when (resource) {
                is Resource.ConnectionError -> Pair(null, resource.message)
                is Resource.ServerError -> Pair(null, resource.message)
                is Resource.NotFoundError -> Pair(null, resource.message)
                is Resource.Success -> Pair(resource.data, null)
            }
        }
    }
}
