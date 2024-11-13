package ru.practicum.android.diploma.domain.models

/**
 * Фильтр для поиска вакансий
 * @param country - страна
 * @param region - регион
 * @param city - город
 * @param industry - отрасль
 * @param salary - ожидаемая зарпала
 * @param onlyWithSalary -  показывать только с зарплатой
 * @param geolocation - показать вакансии рядом со мной (по геолокации)
 * */
data class SearchFilter(
    val country: Area? = null,
    val region: Area? = null,
    val city: Area? = null,
    val industry: Industry? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean? = null,
    val geolocation: Geolocation? = null
)
