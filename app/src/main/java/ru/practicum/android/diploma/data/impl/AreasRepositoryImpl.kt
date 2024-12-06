package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.data.db.converters.AreaRoomToAreaMapper
import ru.practicum.android.diploma.data.db.dao.AreasDao
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.AreaType
import ru.practicum.android.diploma.domain.repository.AreasRepository

class AreasRepositoryImpl(private val dao: AreasDao) : AreasRepository {
    override suspend fun getAllCountries(search: String?): List<Area> {
        return AreaRoomToAreaMapper.map(
            dao.figmaCountries().let { country ->
                val first = country.filter { it.hhPosition > 0 }
                val second = country.filter { it.hhPosition < 0 }
                first + second
            }
        )
    }

    override suspend fun getAllRegions(search: String?, page: Int): List<Area> {
        return AreaRoomToAreaMapper.map(
            if (search.isNullOrBlank()) {
                dao.figmaRegions(
                    offset = offset(page)
                )
            } else {
                dao.figmaRegionsByName(
                    search = search.trim(),
                    offset = offset(page)
                )
            }
        )
    }

    override suspend fun getAllCities(search: String?, page: Int): List<Area> {
        return AreaRoomToAreaMapper.map(
            if (search.isNullOrBlank()) {
                dao.areasByType(
                    type = AreaType.CITY.type,
                    offset = offset(page)
                )
            } else {
                dao.areasByTypeAndName(
                    type = AreaType.CITY.type,
                    search = search.trim(),
                    offset = offset(page)
                )
            }
        )
    }

    override suspend fun getRegionsInCountry(countryId: String, search: String?, page: Int): List<Area> {
        return AreaRoomToAreaMapper.map(
            if (search.isNullOrBlank()) {
                dao.areasByParent(
                    parentId = countryId.toInt(),
                    offset = offset(page)
                )
            } else {
                dao.areasByParentAndName(
                    parentId = countryId.toInt(),
                    search = search.trim(),
                    offset = offset(page)
                )
            }
        )
    }

    override suspend fun getCitiesInCountry(countryId: String, search: String?, page: Int): List<Area> {
        return AreaRoomToAreaMapper.map(
            if (search.isNullOrBlank()) {
                dao.citiesInCountry(
                    countryId = countryId.toInt(),
                    offset = offset(page)
                )
            } else {
                dao.citiesInCountryByName(
                    countryId = countryId.toInt(),
                    search = search.trim(),
                    offset = offset(page)
                )
            }
        )
    }

    override suspend fun getCitiesInRegion(regionId: String, search: String?, page: Int): List<Area> {
        return AreaRoomToAreaMapper.map(
            if (search.isNullOrBlank()) {
                dao.areasByParent(
                    parentId = regionId.toInt(),
                    offset = offset(page)
                )
            } else {
                dao.areasByParentAndName(
                    parentId = regionId.toInt(),
                    search = search.trim(),
                    offset = offset(page)
                )
            }
        )
    }

    override suspend fun getCountry(parentId: String): Area? {
        var country: Area? = null
        var areaId = parentId.toInt()
        while (true) {
            val area = dao.getAreaById(areaId) ?: break
            areaId = area.parentId
            country = AreaRoomToAreaMapper.map(area)
        }
        return country
    }

    override suspend fun getAreaById(id: String): Area? {
        val area = dao.getAreaById(id.toInt())
        return if (area != null) AreaRoomToAreaMapper.map(area) else null
    }

    private fun offset(page: Int): Int {
        return page * AreasDao.AREAS_PER_PAGE
    }
}
