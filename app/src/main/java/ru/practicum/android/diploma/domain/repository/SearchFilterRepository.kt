package ru.practicum.android.diploma.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Geolocation
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.SearchFilter

interface SearchFilterRepository {
    suspend fun getFilter(): Flow<SearchFilter?>

    suspend fun isFilterExists(): Flow<Boolean>

    suspend fun getFilterForNetworkClient(page: Int): SearchFilter?

    suspend fun saveCountry(country: Area?)

    suspend fun saveRegion(region: Area?)

    suspend fun saveCity(city: Area?)

    suspend fun saveIndustry(industry: Industry?)

    suspend fun saveSalary(salary: Int?)

    suspend fun saveOnlyWithSalary(onlyWithSalary: Boolean)

    suspend fun saveGeolocation(geolocation: Geolocation?)

    suspend fun resetFilter()
}
