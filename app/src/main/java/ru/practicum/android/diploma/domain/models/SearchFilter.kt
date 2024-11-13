package ru.practicum.android.diploma.domain.models

class SearchFilter(
    var country: Area? = null, // страна
    var region: Area? = null, // регион
    var city: Area? = null, // город
    var industry: Industry? = null, // Индустрия
    var salary: Int? = null, // ожидаемая зарпала
    var onlyWithSalary: Boolean? = null, // показывать только с зарплатой
    var geolocation: Geolocation? = null // показать вакансии рядом со мной (по геолокации)
)
