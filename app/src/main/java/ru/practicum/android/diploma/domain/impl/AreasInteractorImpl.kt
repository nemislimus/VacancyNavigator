package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.repository.AreasInteractor
import ru.practicum.android.diploma.domain.repository.AreasRepository

class AreasInteractorImpl(private val repository: AreasRepository) : AreasInteractor {
    override suspend fun getAllCountries(search: String?): List<Area> {
        return repository.getAllCountries(search)
    }

    override suspend fun getAllRegions(search: String?, page: Int): List<Area> {
        return repository.getAllRegions(search, page)
    }

    override suspend fun getAllCities(search: String?, page: Int): List<Area> {
        return repository.getAllCities(search, page)
    }

    override suspend fun getRegionsInCountry(countryId: String, search: String?, page: Int): List<Area> {
        return repository.getRegionsInCountry(countryId, search, page)
    }

    override suspend fun getCitiesInCountry(countryId: String, search: String?, page: Int): List<Area> {
        return repository.getCitiesInCountry(countryId, search, page)
    }

    override suspend fun getCitiesInRegion(regionId: String, search: String?, page: Int): List<Area> {
        return repository.getCitiesInRegion(regionId, search, page)
    }

    override suspend fun getCountry(parentId: String): Area? {
        return repository.getCountry(parentId)
    }

    override suspend fun getAreaById(id: String): Area? {
        return repository.getAreaById(id)
    }
}
