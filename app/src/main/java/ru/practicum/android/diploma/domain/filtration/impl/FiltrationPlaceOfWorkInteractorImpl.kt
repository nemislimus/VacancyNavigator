package ru.practicum.android.diploma.domain.filtration.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.domain.filtration.api.FiltrationPlaceOfWorkInteractor
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.repository.AreasRepository

class FiltrationPlaceOfWorkInteractorImpl(private val areasRepository: AreasRepository) :
    FiltrationPlaceOfWorkInteractor {
    override suspend fun getCountryByRegionId(regionId: String): Flow<Area?> = flow {
        emit(areasRepository.getCountry(regionId))
    }.flowOn(Dispatchers.IO)

    override suspend fun getAreaById(id: String): Flow<Area?> = flow {
        emit(areasRepository.getAreaById(id))
    }.flowOn(Dispatchers.IO)
}
