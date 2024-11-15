package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.repository.UpdateDbOnAppStart

class UpdateDbOnAppStartUseCase(private val repository: UpdateDbOnAppStart) {
    suspend operator fun invoke(): Boolean {
        return repository.update()
    }
}
