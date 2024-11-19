package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.models.VacancyShort

interface FavoriteVacancyRepository {
    suspend fun add(vacancy: VacancyFull)

    suspend fun remove(vacancyId: String)

    suspend fun getList(): Flow<List<VacancyShort>>

    suspend fun getById(vacancyId: String): VacancyFull?
}
