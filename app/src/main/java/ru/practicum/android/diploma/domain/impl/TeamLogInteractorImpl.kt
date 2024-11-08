package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.repository.TeamLogInteractor
import ru.practicum.android.diploma.domain.repository.TeamLogRepository

class TeamLogInteractorImpl(private val repository: TeamLogRepository) : TeamLogInteractor {

    override fun d(tag: String, value: String) {
        repository.d(tag, value)
    }

    override fun d(eventName: String, eventParams: Map<String, String>) {
        repository.d(eventName, eventParams)
    }
}
