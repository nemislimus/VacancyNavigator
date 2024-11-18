package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.domain.repository.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.repository.FavoriteVacancyRepository

class FavoriteVacancyInteractorImpl(private val repository: FavoriteVacancyRepository) : FavoriteVacancyInteractor {
    override suspend fun add(vacancy: VacancyFull) {
        repository.add(vacancy)
    }

    override suspend fun remove(vacancyId: Int) {
        repository.remove(vacancyId)
    }

    override suspend fun getList(): Flow<List<VacancyShort>> {
        return repository.getList()
    }

    override suspend fun getById(vacancyId: Int): VacancyFull {
        return repository.getById(vacancyId)
    }
}
