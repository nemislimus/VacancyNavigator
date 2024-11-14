package ru.practicum.android.diploma.domain.models

data class Area(
    val id: String,
    val name: String, // Москва, Воронеж, Сочи
    val type: AreaType, // country, region, city
    val parentId: String? = null
)
