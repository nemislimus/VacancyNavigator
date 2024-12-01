package ru.practicum.android.diploma.ui.filtration.model

import ru.practicum.android.diploma.domain.models.Area

data class WorkPlace(
    val country: Area? = null,
    val region: Area? = null,
)
