package ru.practicum.android.diploma.domain.sharing.impl

import ru.practicum.android.diploma.domain.sharing.api.ExternalNavigator
import ru.practicum.android.diploma.domain.sharing.api.SharingInteractor

class SharingInteractorImpl(
    private val navigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareAppMessageOrLink(text: String) {
        navigator.shareMessageOrLink(text)
    }
}
