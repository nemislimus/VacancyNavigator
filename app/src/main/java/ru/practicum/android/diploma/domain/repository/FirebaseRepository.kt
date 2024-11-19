package ru.practicum.android.diploma.domain.repository

interface FirebaseRepository {
    fun d(tag: String, value: String)

    fun d(eventName: String, eventParams: Map<String, String>)
}
