package ru.practicum.android.diploma.domain.models

data class Salary(
    val from: Int? = null,
    val to: Int? = null,
    val currency: String // RUR, USD
)
