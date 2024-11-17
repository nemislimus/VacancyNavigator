package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.models.SearchFilterRoom
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.AreaType
import ru.practicum.android.diploma.domain.models.Geolocation
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.SearchFilter

object SearchFilterToSearchFilterRoomMapper {
    private const val EMPTY_STRING = ""

    data class DetektArea(
        val id: Int,
        val name: String,
        val parentId: Int
    )

    data class DetektIndustry(
        val id: String,
        val name: String,
        val parentId: Int
    )

    private fun help(area: Area?): DetektArea {
        return DetektArea(
            id = area?.id?.toInt() ?: 0,
            name = area?.name ?: EMPTY_STRING,
            parentId = area?.parentId?.toInt() ?: 0
        )
    }

    private fun help(industry: Industry?): DetektIndustry {
        return DetektIndustry(
            id = industry?.id ?: "",
            name = industry?.name ?: EMPTY_STRING,
            parentId = industry?.parentId?.toInt() ?: 0
        )
    }

    fun map(filter: SearchFilter): SearchFilterRoom {
        val country = help(filter.country)
        val region = help(filter.region)
        val city = help(filter.city)
        val industry = help(filter.industry)

        return SearchFilterRoom(
            filterId = 1,
            countryId = country.id,
            countryName = country.name,
            countryParentId = country.parentId,
            regionId = region.id,
            regionName = region.name,
            regionParentId = region.parentId,
            cityId = city.id,
            cityName = city.name,
            cityParentId = city.parentId,
            industryId = industry.id,
            industryName = industry.name,
            industryParentId = industry.parentId,
            salary = filter.salary ?: -1,
            onlyWithSalary = if (filter.onlyWithSalary) 1 else 0,
            lat = filter.geolocation?.lat ?: "",
            lng = filter.geolocation?.lng ?: ""
        )
    }

    fun map(filter: SearchFilterRoom?): SearchFilter? {
        filter?.let {
            return SearchFilter(
                country = country(filter),
                region = region(filter),
                city = city(filter),
                industry = industry(filter),
                salary = if (filter.salary == -1) null else filter.salary,
                onlyWithSalary = (filter.onlyWithSalary == 1),
                geolocation = geolocation(filter)
            )
        } ?: run {
            return null
        }
    }

    private fun country(filter: SearchFilterRoom): Area? {
        with(filter) {
            return if (countryId > 0) {
                Area(
                    id = countryId.toString(),
                    name = countryName,
                    type = AreaType.COUNTRY,
                    parentId = if (countryParentId > 0) {
                        countryParentId.toString()
                    } else {
                        null
                    }
                )
            } else {
                null
            }
        }
    }

    private fun region(filter: SearchFilterRoom): Area? {
        with(filter) {
            return if (regionId > 0) {
                Area(
                    id = regionId.toString(),
                    name = regionName,
                    type = AreaType.COUNTRY,
                    parentId = if (regionParentId > 0) {
                        regionParentId.toString()
                    } else {
                        null
                    }
                )
            } else {
                null
            }
        }
    }

    private fun city(filter: SearchFilterRoom): Area? {
        with(filter) {
            return if (cityId > 0) {
                Area(
                    id = cityId.toString(),
                    name = cityName,
                    type = AreaType.COUNTRY,
                    parentId = if (cityParentId > 0) {
                        cityParentId.toString()
                    } else {
                        null
                    }
                )
            } else {
                null
            }
        }
    }

    private fun industry(filter: SearchFilterRoom): Industry? {
        with(filter) {
            return if (industryId.isNotBlank()) {
                Industry(
                    id = industryId,
                    name = industryName,
                    parentId = if (industryParentId > 0) {
                        industryParentId.toString()
                    } else {
                        null
                    }
                )
            } else {
                null
            }
        }
    }

    private fun geolocation(filter: SearchFilterRoom): Geolocation? {
        with(filter) {
            return if (lat.isNotBlank() && lng.isNotBlank()) {
                Geolocation(
                    lat = lat,
                    lng = lng
                )
            } else {
                null
            }
        }
    }
}
