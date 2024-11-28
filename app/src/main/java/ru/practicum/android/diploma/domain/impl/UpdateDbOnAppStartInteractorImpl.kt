package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.repository.UpdateDbOnAppStartInteractor
import ru.practicum.android.diploma.domain.repository.UpdateDbOnAppStartRepository

class UpdateDbOnAppStartInteractorImpl(private val repository: UpdateDbOnAppStartRepository) :
    UpdateDbOnAppStartInteractor {
    override suspend fun update(): Boolean {
        return repository.update()
    }

    override suspend fun setNoInternet() {
        repository.setNoInternet()
    }
}
