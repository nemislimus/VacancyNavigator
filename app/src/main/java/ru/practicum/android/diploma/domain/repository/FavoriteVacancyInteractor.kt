package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.models.VacancyShort

interface FavoriteVacancyInteractor {
    suspend fun add(vacancy: VacancyFull)

    suspend fun remove(vacancyId: Int)

    suspend fun getList(): Flow<List<VacancyShort>>

    suspend fun getById(vacancyId: Int): VacancyFull
}
