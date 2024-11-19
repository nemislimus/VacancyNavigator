package ru.practicum.android.diploma.domain.models

data class VacancyList(
    val items: List<VacancyShort>,
    val found: Int, // общее число найденных вакансий
    val pages: Int, // число найденных страниц
    val perPage: Int, // число вакансий на странице
    val page: Int // текущая страница результатов поиска
)
