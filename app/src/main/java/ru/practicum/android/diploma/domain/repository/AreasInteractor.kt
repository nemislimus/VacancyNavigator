package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.Area

interface AreasInteractor {
    suspend fun getAllCountries(search: String? = null): List<Area>

    suspend fun getAllRegions(search: String? = null): List<Area>

    suspend fun getAllCities(search: String? = null): List<Area>

    suspend fun getRegionsInCountry(countryId: String, search: String? = null): List<Area>

    suspend fun getCitiesInCountry(countryId: String, search: String? = null): List<Area>

    suspend fun getCitiesInRegion(regionId: String, search: String? = null): List<Area>

    suspend fun countCitiesInCountry(countryId: String): Int

    suspend fun countCitiesInRegion(regionId: String): Int

    suspend fun countRegionsInCountry(countryId: String): Int
}
