package ru.practicum.android.diploma.domain.repository

interface TeamLogRepository {
    fun d(tag: String, value: String)

    fun d(eventName: String, eventParams: Map<String, String>)
}
