package ru.practicum.android.diploma.domain.models

data class SearchFilter(
    val country: Area? = null, // страна
    val region: Area? = null, // регион
    val city: Area? = null, // город
    val industry: Industry? = null, // Индустрия
    val salary: Int? = null, // ожидаемая зарпала
    val onlyWithSalary: Boolean? = null, // показывать только с зарплатой
    val geolocation: Geolocation? = null // показать вакансии рядом со мной (по геолокации)
)
