package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.data.db.converters.AreaRoomToAreaMapper
import ru.practicum.android.diploma.data.db.converters.SearchFilterToSearchFilterRoomMapper
import ru.practicum.android.diploma.data.db.dao.SearchFilterDao
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Geolocation
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.SearchFilter
import ru.practicum.android.diploma.domain.repository.SetSearchFilterRepository

class SetSearchFilterRepositoryImpl(private val dao: SearchFilterDao) : SetSearchFilterRepository {
    private val mapper = SearchFilterToSearchFilterRoomMapper

    override suspend fun saveCountry(country: Area?) {
        val filter = getFilterOrDefault()
        filter.country = country
        dao.replaceFilter(mapper.map(filter))
    }

    override suspend fun saveRegion(region: Area?) {
        val filter = getFilterOrDefault()
        filter.region = region

        if (region?.parentId != null) {
            dao.getParentArea(region.parentId.toInt())?.let { roomCountry ->
                filter.country = AreaRoomToAreaMapper.map(roomCountry)
            } ?: run {
                filter.country = null
            }
        }

        dao.replaceFilter(mapper.map(filter))
    }

    override suspend fun saveCity(city: Area?) {
        val filter = getFilterOrDefault()

        filter.city = city

        if (city?.parentId != null) {
            dao.getParentArea(city.parentId.toInt())?.let { roomRegion ->
                filter.region = AreaRoomToAreaMapper.map(roomRegion)
            } ?: run {
                filter.region = null
            }
        }

        val regionParentId = filter.region?.parentId

        if (regionParentId != null) {
            dao.getParentArea(regionParentId.toInt())?.let { roomCountry ->
                filter.country = AreaRoomToAreaMapper.map(roomCountry)
            } ?: run {
                filter.country = null
            }
        }

        dao.replaceFilter(mapper.map(filter))
    }

    override suspend fun saveIndustry(industry: Industry?) {
        val filter = getFilterOrDefault()

        filter.industry = industry

        dao.replaceFilter(mapper.map(filter))
    }

    override suspend fun saveSalary(salary: Int?) {
        val filter = getFilterOrDefault()
        filter.salary = salary
        dao.replaceFilter(mapper.map(filter))
    }

    override suspend fun saveOnlyWithSalary(onlyWithSalary: Boolean) {
        val filter = getFilterOrDefault()
        filter.onlyWithSalary = onlyWithSalary
        dao.replaceFilter(mapper.map(filter))
    }

    override suspend fun saveGeolocation(geolocation: Geolocation?) {
        val filter = getFilterOrDefault()
        filter.geolocation = geolocation
        dao.replaceFilter(mapper.map(filter))
    }

    override suspend fun resetFilter() {
        dao.deleteFilter()
    }

    private suspend fun getFilterOrDefault(): SearchFilter {
        dao.getFilter()?.let { filter ->
            mapper.map(filter)?.let {
                return it
            }
        }

        return SearchFilter()
    }
}
