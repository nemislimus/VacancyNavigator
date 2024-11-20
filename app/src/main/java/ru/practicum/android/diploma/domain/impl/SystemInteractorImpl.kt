package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.repository.SystemInteractor
import ru.practicum.android.diploma.domain.repository.SystemRepository

class SystemInteractorImpl(private val repository: SystemRepository) : SystemInteractor {
    override fun getString(id: Int): String {
        return repository.getString(id)
    }

    override fun showToast(message: String) {
        repository.showToast(message)
    }
}
