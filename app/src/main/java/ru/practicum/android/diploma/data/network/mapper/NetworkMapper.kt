package ru.practicum.android.diploma.data.network.mapper

import ru.practicum.android.diploma.domain.models.SearchFilter

class NetworkMapper {

    fun map(text: String, page: Int = 0, searchFilter: SearchFilter?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["text"] = text
        map["page"] = page.toString()

        searchFilter?.let { params ->
            params.salary?.let {
                map["salary"] = it.toString()
            }

            params.onlyWithSalary?.let {
                map["only_with_salary"] = it.toString()
            }

            // тут порядок важен. Если задан город, то ищем вначале в городе, потом в регионе, потом в стране
            if (params.city != null) {
                map["area"] = params.city.id
            } else if (params.region != null) {
                map["area"] = params.region.id
            } else if (params.country != null) {
                map["area"] = params.country.id
            }

            params.industry?.let {
                map["industry"] = it.id
            }

            params.geolocation?.let {
                map["order_by"] = "distance"
                map["sort_point_lat"] = it.lat
                map["sort_point_lng"] = it.lng
            }
        }

        return map
    }
}
