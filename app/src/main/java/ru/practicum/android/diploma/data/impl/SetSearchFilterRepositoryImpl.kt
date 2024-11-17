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
        setFilterOrNull(
            filter = getFilterOrDefault().copy(country = country).copy(region = null).copy(city = null)
        )
    }

    override suspend fun saveRegion(region: Area?) {
        var country: Area? = null

        if (region?.parentId != null) {
            dao.getParentArea(region.parentId.toInt())?.let { roomCountry ->
                country = AreaRoomToAreaMapper.map(roomCountry)
            }
        }

        setFilterOrNull(
            filter = getFilterOrDefault().copy(country = country).copy(region = region).copy(city = null)
        )
    }

    override suspend fun saveCity(city: Area?) {
        var country: Area? = null
        var region: Area? = null

        if (city?.parentId != null) {
            dao.getParentArea(city.parentId.toInt())?.let { roomRegion ->
                region = AreaRoomToAreaMapper.map(roomRegion)
            }
        }

        val regionParentId = region?.parentId

        if (regionParentId != null) {
            dao.getParentArea(regionParentId.toInt())?.let { roomCountry ->
                country = AreaRoomToAreaMapper.map(roomCountry)
            }
        }

        setFilterOrNull(
            filter = getFilterOrDefault().copy(country = country).copy(region = region).copy(city = city)
        )
    }

    override suspend fun saveIndustry(industry: Industry?) {
        setFilterOrNull(
            filter = getFilterOrDefault().copy(industry = industry)
        )
    }

    override suspend fun saveSalary(salary: Int?) {
        setFilterOrNull(
            filter = getFilterOrDefault().copy(salary = salary)
        )
    }

    override suspend fun saveOnlyWithSalary(onlyWithSalary: Boolean) {
        setFilterOrNull(
            filter = getFilterOrDefault().copy(onlyWithSalary = onlyWithSalary)
        )
    }

    override suspend fun saveGeolocation(geolocation: Geolocation?) {
        setFilterOrNull(
            filter = getFilterOrDefault().copy(geolocation = geolocation)
        )
    }

    override suspend fun resetFilter() {
        dao.deleteFilter()
    }

    private suspend fun setFilterOrNull(filter: SearchFilter) {
        if (filter == SearchFilter()) {
            dao.deleteFilter()
        } else {
            dao.replaceFilter(mapper.map(filter))
        }
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
