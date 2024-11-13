package ru.practicum.android.diploma.domain.models

data class VacancyFull(
    val id: String,
    val name: String, // Backend разработчик (стажер), Программист (Junior)
    val employer: String, // Яндекс, Газпром
    val areaName: String, // Москва, Воронеж, Сочи
    val iconUrl: String? = null,
    val salary: Salary? = null,
    val experience: String, // От 1 года до 3 лет, Нет опыта
    val employment: String, // Стажировка, Полная занятость
    val schedule: String, // Удаленная работа, Полный день,
    val description: String, // "<p><strong>ics-it</strong> — команда экспертов в ...</p>
    val geolocation: Geolocation? = null
)
