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
        val filter = getFilterOrDefault().copy(country = country).copy(region = null).copy(city = null)
        dao.replaceFilter(mapper.map(filter))
    }

    override suspend fun saveRegion(region: Area?) {
        var country: Area? = null

        if (region?.parentId != null) {
            dao.getParentArea(region.parentId.toInt())?.let { roomCountry ->
                country = AreaRoomToAreaMapper.map(roomCountry)
            }
        }

        val filter = getFilterOrDefault().copy(country = country).copy(region = region).copy(city = null)

        dao.replaceFilter(mapper.map(filter))
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

        val filter = getFilterOrDefault().copy(country = country).copy(region = region).copy(city = city)

        dao.replaceFilter(mapper.map(filter))
    }

    override suspend fun saveIndustry(industry: Industry?) {
        val filter = getFilterOrDefault().copy(industry = industry)

        dao.replaceFilter(mapper.map(filter))
    }

    override suspend fun saveSalary(salary: Int?) {
        val filter = getFilterOrDefault().copy(salary = salary)
        dao.replaceFilter(mapper.map(filter))
    }

    override suspend fun saveOnlyWithSalary(onlyWithSalary: Boolean) {
        val filter = getFilterOrDefault().copy(onlyWithSalary = onlyWithSalary)
        dao.replaceFilter(mapper.map(filter))
    }

    override suspend fun saveGeolocation(geolocation: Geolocation?) {
        val filter = getFilterOrDefault().copy(geolocation = geolocation)
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
