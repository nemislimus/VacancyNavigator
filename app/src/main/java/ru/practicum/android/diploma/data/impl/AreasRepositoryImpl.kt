package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.data.db.converters.AreaRoomToAreaMapper
import ru.practicum.android.diploma.data.db.dao.AreasDao
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.AreaType
import ru.practicum.android.diploma.domain.repository.AreasRepository

class AreasRepositoryImpl(private val dao: AreasDao) : AreasRepository {
    override suspend fun getAllCountries(search: String?): List<Area> {
        search?.let {
            return AreaRoomToAreaMapper.map(
                dao.getAllAreasByTypeAndName(
                    AreaType.COUNTRY.type,
                    search
                )
            )
        } ?: run {
            return AreaRoomToAreaMapper.map(
                dao.getAllAreasByType(
                    AreaType.COUNTRY.type
                )
            )
        }
    }

    override suspend fun getAllRegions(search: String?): List<Area> {
        search?.let {
            return AreaRoomToAreaMapper.map(
                dao.getAllAreasByTypeAndName(
                    AreaType.REGION.type,
                    search
                )
            )
        } ?: run {
            return AreaRoomToAreaMapper.map(
                dao.getAllAreasByType(
                    AreaType.REGION.type
                )
            )
        }
    }

    override suspend fun getAllCities(search: String?): List<Area> {
        search?.let {
            return AreaRoomToAreaMapper.map(
                dao.getAllAreasByTypeAndName(
                    AreaType.CITY.type,
                    search
                )
            )
        } ?: run {
            return AreaRoomToAreaMapper.map(
                dao.getAllAreasByType(
                    AreaType.CITY.type
                )
            )
        }
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

    companion object {
        const val MOSCOW_ID = 1
    }
}
