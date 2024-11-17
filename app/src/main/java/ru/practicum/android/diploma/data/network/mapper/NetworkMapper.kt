package ru.practicum.android.diploma.data.network.mapper

import ru.practicum.android.diploma.data.network.ApiResponse
import ru.practicum.android.diploma.data.search.dto.model.SalaryDto
import ru.practicum.android.diploma.data.search.dto.model.VacancyDto
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.VacancyList
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.domain.search.model.SearchVacancyOptions

/*
* Описание всех возможных полей тут https://api.hh.ru/openapi/redoc#tag/Poisk-vakansij/operation/get-vacancies
* */

class NetworkMapper {

    fun map(searchVacancyOptions: SearchVacancyOptions): Map<String, String> {
        val map = mutableMapOf<String, String>()

        map["text"] = searchVacancyOptions.text
        map["page"] = searchVacancyOptions.page.toString()
        map["per_page"] = VACANCIES_PER_PAGE
        map["no_magic"] = "true"

        searchVacancyOptions.filter?.let { params ->
            params.salary?.let { map["salary"] = it.toString() }

            params.onlyWithSalary?.let { map["only_with_salary"] = it.toString() }

            params.industry?.let { map["industry"] = it.id }

            params.geolocation?.let {
                map["order_by"] = "distance"
                map["sort_point_lat"] = it.lat
                map["sort_point_lng"] = it.lng
            } ?: run {
                // тут порядок важен. Если задан город, то ищем вначале в городе, потом в регионе, потом в стране
                (params.city?.id ?: params.region?.id ?: params.country?.id)?.let { map["area"] = it }
            }
        }

        return map
    }

    fun map(response: ApiResponse.VacancyResponse): VacancyList {
        val items: List<VacancyShort> = response.items.map { map(it) }
        return VacancyList(
            items = items,
            found = response.found ?: 0,
            pages = response.pages ?: 0,
            perPage = response.perPage ?: 0,
            page = response.page ?: 0
        )
    }

    fun map(vacancyDto: VacancyDto): VacancyShort {
        return VacancyShort(
            id = vacancyDto.id,
            name = vacancyDto.name,
            employer = vacancyDto.employer?.name ?: "",
            areaName = vacancyDto.area?.name ?: "",
            iconUrl = vacancyDto.employer?.logoUrls?.s90,
            salary = vacancyDto.salary?.let { map(it) }
        )
    }

    fun map(salaryDto: SalaryDto): Salary {
        return Salary(
            from = salaryDto.from,
            to = salaryDto.to,
            currency = salaryDto.currency ?: ""
        )
    }

    companion object {
        const val VACANCIES_PER_PAGE = "20"
    }
}
