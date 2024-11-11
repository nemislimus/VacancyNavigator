package ru.practicum.android.diploma.data.dto

data class AreaDto(
    val id: String,
    val parent_id: String,
    val name: String,
    val areas: List<AreaDto>,
)
