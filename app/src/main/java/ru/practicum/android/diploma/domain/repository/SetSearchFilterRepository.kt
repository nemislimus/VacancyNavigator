package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Geolocation
import ru.practicum.android.diploma.domain.models.Industry

interface SetSearchFilterRepository {
    suspend fun saveIndustry(industry: Industry?)

    suspend fun saveSalary(salary: Int?)

    suspend fun saveOnlyWithSalary(onlyWithSalary: Boolean)

    suspend fun saveGeolocation(geolocation: Geolocation?)

    suspend fun resetFilter()

    suspend fun saveArea(area: Area, saveToTempFilter: Boolean = false)

    suspend fun resetAreaTempValue()
}
