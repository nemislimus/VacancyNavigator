package ru.practicum.android.diploma.domain.models

data class VacancyShort(
    val id: String,
    val name: String, // Backend разработчик (стажер), Программист (Junior)
    val employer: String, // Яндекс, Газпром
    val areaName: String, // Москва, Воронеж, Сочи
    val iconUrl: String? = null,
    val salary: Salary? = null,
    val geolocation: Geolocation? = null
)
