package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.data.db.dao.AreasDao
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.repository.AreasRepository

class AreasRepositoryImpl(private val dao: AreasDao) : AreasRepository {
    override suspend fun getAllCountries(search: String?): List<Area> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllRegions(search: String?): List<Area> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCities(search: String?): List<Area> {
        TODO("Not yet implemented")
    }

    override suspend fun getRegionsInCountry(countryId: String, search: String?): List<Area> {
        TODO("Not yet implemented")
    }

    override suspend fun getCitiesInCountry(countryId: String, search: String?): List<Area> {
        TODO("Not yet implemented")
    }

    override suspend fun getCitiesInRegion(regionId: String, search: String?): List<Area> {
        TODO("Not yet implemented")
    }
}
