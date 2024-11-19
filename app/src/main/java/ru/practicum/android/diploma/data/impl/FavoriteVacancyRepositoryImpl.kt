package ru.practicum.android.diploma.data.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.converters.VacancyFullToFavoriteVacancyRoomMapper
import ru.practicum.android.diploma.data.db.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.domain.repository.FavoriteVacancyRepository

class FavoriteVacancyRepositoryImpl(private val dao: FavoriteVacancyDao) : FavoriteVacancyRepository {
    private val mapper = VacancyFullToFavoriteVacancyRoomMapper

    override suspend fun add(vacancy: VacancyFull) {
        dao.add(
            mapper.map(vacancy)
        )
    }

    override suspend fun remove(vacancyId: String) {
        dao.remove(vacancyId.toInt())
    }

    override suspend fun getList(): Flow<List<VacancyShort>> {
        return dao.getList().map { mapper.map(it) }
    }

    override suspend fun getById(vacancyId: String): VacancyFull? {
        return mapper.toFull(dao.getById(vacancyId.toInt()))
    }
}
