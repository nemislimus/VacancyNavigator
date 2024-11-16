package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.repository.UpdateDbOnAppStartRepository

class UpdateDbOnAppStartUseCase(private val repository: UpdateDbOnAppStartRepository) {
    suspend operator fun invoke(): Boolean {
        return repository.update()
    }
}
