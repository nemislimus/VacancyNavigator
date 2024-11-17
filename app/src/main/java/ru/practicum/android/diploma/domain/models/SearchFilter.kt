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
    var country: Area? = null,
    var region: Area? = null,
    var city: Area? = null,
    var industry: Industry? = null,
    var salary: Int? = null,
    var onlyWithSalary: Boolean = false,
    var geolocation: Geolocation? = null
)
