package ru.practicum.android.diploma.domain.filtration.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Area

interface FiltrationPlaceOfWorkInteractor {
    suspend fun getCountryByRegionId(regionId: String): Flow<Area?>
}
