package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.domain.repository.FavoriteVacancyRepository

class FavoriteVacancyRepositoryImpl : FavoriteVacancyRepository {
    override suspend fun add(vacancy: VacancyFull) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(vacancyId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getList(): Flow<List<VacancyShort>> {
        TODO("Not yet implemented")
    }

    override suspend fun getById(vacancyId: Int): VacancyFull {
        TODO("Not yet implemented")
    }
}
