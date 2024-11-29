package ru.practicum.android.diploma.domain.sharing.impl

import ru.practicum.android.diploma.domain.sharing.api.ExternalNavigatorRepository
import ru.practicum.android.diploma.domain.sharing.api.SharingInteractor

class SharingInteractorImpl(
    private val navigator: ExternalNavigatorRepository,
) : SharingInteractor {
    override fun shareAppMessageOrLink(text: String, shareDialogTitle: String) {
        navigator.shareMessageOrLink(text, shareDialogTitle)
    }
}
