package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.data.db.converters.AreaRoomToAreaMapper
import ru.practicum.android.diploma.data.db.converters.SearchFilterToSearchFilterRoomMapper
import ru.practicum.android.diploma.data.db.dao.SearchFilterDao
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Geolocation
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.SearchFilter
import ru.practicum.android.diploma.domain.repository.SetSearchFilterRepository

class SetSearchFilterRepositoryImpl(
    private val dao: SearchFilterDao
) :
    SetSearchFilterRepository {
    private val mapper = SearchFilterToSearchFilterRoomMapper

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

    override suspend fun saveArea(area: Area?, saveToTempFilter: Boolean) {
        var oldFilter = getFilterOrDefault().copy(country = null).copy(region = null).copy(city = null)

        area?.let {
            val list: MutableList<Area> = mutableListOf(area)
            var parentId = area.parentId?.toInt() ?: 0
            while (true) {
                val areaItem = dao.getParentArea(parentId) ?: break
                parentId = areaItem.parentId
                list.add(0, AreaRoomToAreaMapper.map(areaItem))
            }

            list.forEachIndexed { index, areaItem ->
                oldFilter = when (index) {
                    0 -> oldFilter.copy(country = areaItem)
                    1 -> oldFilter.copy(region = areaItem)
                    2 -> oldFilter.copy(city = areaItem)
                    else -> oldFilter
                }
            }
        }

        if (saveToTempFilter) {
            setTempFilter(oldFilter)
        } else {
            setOrReset(oldFilter)
        }
    }

    override suspend fun resetAreaTempValue() {
        dao.deleteTempFilter()
    }

    private suspend fun setOrReset(newFilter: SearchFilter) {
        if (newFilter == SearchFilter()) {
            dao.deleteFilter()
        } else {
            dao.replaceFilter(mapper.map(newFilter))
        }
    }

    private suspend fun setTempFilter(newFilter: SearchFilter) {
        dao.replaceFilter(mapper.map(newFilter).copy(filterId = 2))
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
