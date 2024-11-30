package ru.practicum.android.diploma.domain.repository

import ru.practicum.android.diploma.domain.models.Area

interface AreasInteractor {
    suspend fun getAllCountries(search: String? = null): List<Area>

    suspend fun getAllRegions(search: String? = null, page: Int): List<Area>

    suspend fun getAllCities(search: String? = null, page: Int): List<Area>

    suspend fun getRegionsInCountry(countryId: String, search: String? = null, page: Int): List<Area>

    suspend fun getCitiesInCountry(countryId: String, search: String? = null, page: Int): List<Area>

    suspend fun getCitiesInRegion(regionId: String, search: String? = null, page: Int): List<Area>

    suspend fun getCountry(parentId: String): Area?

    suspend fun getAreaById(id: String): Area?
}
