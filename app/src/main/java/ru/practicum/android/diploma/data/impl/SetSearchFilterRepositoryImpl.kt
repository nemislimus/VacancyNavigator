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
        val oldFilter = getFilterOrDefault()
        if (oldFilter.country == country) {
            return
        }
        setOrReset(
            newFilter = oldFilter.copy(country = country).copy(region = null).copy(city = null)
        )
    }

    override suspend fun saveRegion(region: Area?) {
        val oldFilter = getFilterOrDefault()
        if (oldFilter.region == region) {
            return
        }
        var country: Area? = null

        if (region?.parentId != null) {
            dao.getParentArea(region.parentId.toInt())?.let { roomCountry ->
                country = AreaRoomToAreaMapper.map(roomCountry)
            }
        }

        setOrReset(
            newFilter = oldFilter.copy(country = country).copy(region = region).copy(city = null)
        )
    }

    override suspend fun saveCity(city: Area?) {
        val oldFilter = getFilterOrDefault()
        if (oldFilter.city == city) {
            return
        }
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

        setOrReset(
            newFilter = oldFilter.copy(country = country).copy(region = region).copy(city = city)
        )
    }

    override suspend fun saveIndustry(industry: Industry?) {
        setOrReset(
            newFilter = getFilterOrDefault().copy(industry = industry)
        )
    }

    override suspend fun saveSalary(salary: Int?) {
        setOrReset(
            newFilter = getFilterOrDefault().copy(salary = salary)
        )
    }

    override suspend fun saveOnlyWithSalary(onlyWithSalary: Boolean) {
        setOrReset(
            newFilter = getFilterOrDefault().copy(onlyWithSalary = onlyWithSalary)
        )
    }

    override suspend fun saveGeolocation(geolocation: Geolocation?) {
        setOrReset(
            newFilter = getFilterOrDefault().copy(geolocation = geolocation)
        )
    }

    override suspend fun resetFilter() {
        dao.deleteFilter()
    }

    private suspend fun setOrReset(newFilter: SearchFilter) {
        if (newFilter == SearchFilter()) {
            dao.deleteFilter()
        } else {
            dao.replaceFilter(mapper.map(newFilter))
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
