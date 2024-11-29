package ru.practicum.android.diploma.domain.sharing.api

interface ExternalNavigatorRepository {
    fun shareMessageOrLink(messageOrLink: String, shareDialogTitle: String = "")
}
