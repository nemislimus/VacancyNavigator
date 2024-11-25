package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.repository.AreasInteractor
import ru.practicum.android.diploma.domain.repository.AreasRepository

class AreasInteractorImpl(private val repository: AreasRepository) : AreasInteractor {
    override suspend fun getAllCountries(search: String?): List<Area> {
        return repository.getAllCountries(search)
    }

    override suspend fun getAllRegions(search: String?): List<Area> {
        return repository.getAllRegions(search)
    }

    override suspend fun getAllCities(search: String?): List<Area> {
        return repository.getAllCities(search)
    }

    override suspend fun getRegionsInCountry(countryId: String, search: String?): List<Area> {
        return repository.getCitiesInCountry(countryId, search)
    }

    override suspend fun getCitiesInCountry(countryId: String, search: String?): List<Area> {
        return repository.getCitiesInCountry(countryId, search)
    }

    override suspend fun getCitiesInRegion(regionId: String, search: String?): List<Area> {
        return repository.getCitiesInRegion(regionId, search)
    }

    override suspend fun countCitiesInCountry(countryId: String): Int {
        return repository.countCitiesInCountry(countryId)
    }

    override suspend fun countCitiesInRegion(regionId: String): Int {
        return repository.countCitiesInRegion(regionId)
    }

    override suspend fun countRegionsInCountry(countryId: String): Int {
        return repository.countRegionsInCountry(countryId)
    }

    override suspend fun getCountry(parentId: String): Area? {
        return repository.getCountry(parentId)
    }
}
