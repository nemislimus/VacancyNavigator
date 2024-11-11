package ru.practicum.android.diploma.data.network.mapper

import ru.practicum.android.diploma.domain.search.model.SearchVacancyOptions

class NetworkMapper {

    fun map(searchVacancyOptions: SearchVacancyOptions): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["text"] = searchVacancyOptions.text
        map["page"] = searchVacancyOptions.page.toString()
        searchVacancyOptions.areaId?.let {
            map["area"] = it
        }
        searchVacancyOptions.industryId?.let {
            map["industry"] = it
        }
        searchVacancyOptions.salary?.let {
            map["salary"] = it.toString()
        }
        searchVacancyOptions.areaId?.let {
            map["area"] = it
        }
        searchVacancyOptions.onlyWithSalary?.let {
            map["area"] = it.toString()
        }

        return map

    }
}
