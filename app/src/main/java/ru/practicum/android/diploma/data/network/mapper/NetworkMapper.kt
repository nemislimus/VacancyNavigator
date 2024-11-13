package ru.practicum.android.diploma.data.network.mapper

import ru.practicum.android.diploma.domain.search.model.SearchVacancyOptions

class NetworkMapper {

    fun map(searchVacancyOptions: SearchVacancyOptions): Map<String, String> {
        val map = mutableMapOf<String, String>()

        map["text"] = searchVacancyOptions.text
        map["page"] = searchVacancyOptions.page.toString()

        searchVacancyOptions.filter.let { params ->
            params.salary?.let { map["salary"] = it.toString() }

            params.onlyWithSalary?.let { map["only_with_salary"] = it.toString() }

            // тут порядок важен. Если задан город, то ищем вначале в городе, потом в регионе, потом в стране
            (params.city?.id ?: params.region?.id ?: params.country?.id)?.let { map["area"] = it }

            params.industry?.let { map["industry"] = it.id }

            params.geolocation?.let {
                map["order_by"] = "distance"
                map["sort_point_lat"] = it.lat
                map["sort_point_lng"] = it.lng
            }
        }

        return map
    }
}
