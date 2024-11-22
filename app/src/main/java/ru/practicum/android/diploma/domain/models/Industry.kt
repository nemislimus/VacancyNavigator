package ru.practicum.android.diploma.domain.models

data class Industry(
    val id: String,
    val name: String,
    val parentId: String?,
    var isSelected: Boolean = false // Это поле нужно для работы в адаптере выбора отраслей
)
