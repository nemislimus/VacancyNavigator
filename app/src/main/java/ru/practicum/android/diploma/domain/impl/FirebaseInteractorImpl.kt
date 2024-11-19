package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.repository.FirebaseInteractor
import ru.practicum.android.diploma.domain.repository.FirebaseRepository

class FirebaseInteractorImpl(private val repository: FirebaseRepository) : FirebaseInteractor {

    override fun d(tag: String, value: String) {
        repository.d(tag, value)
    }

    override fun d(eventName: String, eventParams: Map<String, String>) {
        repository.d(eventName, eventParams)
    }
}
