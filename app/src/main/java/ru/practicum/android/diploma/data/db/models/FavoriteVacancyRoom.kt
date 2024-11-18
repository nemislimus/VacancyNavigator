package ru.practicum.android.diploma.data.db.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "favorite_vacancy",
    indices = [
        Index(value = ["lastMod"])
    ]
)
data class FavoriteVacancyRoom(
    @PrimaryKey val id: Int,
    val name: String, // Backend разработчик (стажер), Программист (Junior)
    val employer: String, // Яндекс, Газпром
    val areaName: String, // Москва, Воронеж, Сочи
    val iconUrl: String,
    val from: Int,
    val to: Int,
    val currency: String,
    val experience: String, // От 1 года до 3 лет, Нет опыта
    val employment: String, // Стажировка, Полная занятость
    val schedule: String, // Удаленная работа, Полный день,
    val description: String, // "<p><strong>ics-it</strong> — команда экспертов в ...</p>
    val keySkills: String, // ["Прием посетителей", "Первичный документооборот"], список может быть пустым []
    val address: String, // Москва, Годовикова 9, стр. 10 (территория завода «Калибр»)
    val lat: String, // 54.997006
    val lng: String,
    val urlHh: String, // https://hh.ru/vacancy/110194878
    val lastMod: Int
)
