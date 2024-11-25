package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Geolocation
import ru.practicum.android.diploma.domain.models.Industry

interface SetSearchFilterInteractor {

    suspend fun saveIndustry(industry: Industry?)

    suspend fun saveSalary(salary: Int?)

    suspend fun saveOnlyWithSalary(onlyWithSalary: Boolean)

    suspend fun saveGeolocation(geolocation: Geolocation?)

    suspend fun resetFilter()

    /**
     * Установка фильтра на место работы
     * @param area - [Area] страна, регион или город
     * */
    suspend fun saveArea(area: Area)

    /**
     * Сохранить временное значение страны, города, региона
     * @param area - [Area] страна, регион или город
     * */
    suspend fun saveAreaTempValue(area: Area)

    /**
     * Удалить временное значение страны, города, региона
     * */
    suspend fun resetAreaTempValue()
}
