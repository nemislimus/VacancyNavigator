package ru.practicum.android.diploma.domain.sharing.api

interface ExternalNavigator {
    fun shareMessageOrLink(messageOrLink: String)
}
