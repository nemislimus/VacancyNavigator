package ru.practicum.android.diploma.domain.models

/**
 * Фильтр для поиска вакансий
 * @param country - [Area] страна
 * @param region - [Area] регион
 * @param city - [Area] город
 * @param industry - [Industry] отрасль
 * @param salary - [Int] ожидаемая зарпала
 * @param onlyWithSalary - [Boolean] показывать только с зарплатой
 * @param geolocation - [Geolocation] показать вакансии рядом со мной (по геолокации)
 * @author Киреенко А.
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
