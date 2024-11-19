package ru.practicum.android.diploma.data.network.mapper

import ru.practicum.android.diploma.data.network.ApiResponse
import ru.practicum.android.diploma.data.search.dto.model.AddressDto
import ru.practicum.android.diploma.data.search.dto.model.AreaDto
import ru.practicum.android.diploma.data.search.dto.model.EmployerDto
import ru.practicum.android.diploma.data.search.dto.model.SalaryDto
import ru.practicum.android.diploma.data.search.dto.model.VacancyDto
import ru.practicum.android.diploma.domain.models.Geolocation
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.VacancyFull
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

    fun map(salaryDto: SalaryDto?): Salary? {
        return salaryDto?.let {
            Salary(
                from = it.from,
                to = it.to,
                currency = it.currency ?: ""
            )
        }
    }

    fun map(vacancyDto: VacancyDto): VacancyShort {
        return VacancyShort(
            id = vacancyDto.id,
            name = vacancyDto.name,
            employer = vacancyDto.employer?.name ?: "",
            areaName = getAreaName(vacancyDto.area),
            iconUrl = getEmployerLogo(vacancyDto.employer),
            salary = map(vacancyDto.salary),
            geolocation = vacancyDto.address?.let { map(it) }
        )
    }

    fun map(vacDto: ApiResponse.VacancyDetailedResponse): VacancyFull =
        VacancyFull(
            id = vacDto.id,
            name = vacDto.name,
            employer = vacDto.employer?.name ?: "",
            areaName = getAreaName(vacDto.area),
            iconUrl = getEmployerLogo(vacDto.employer),
            salary = map(vacDto.salary),
            experience = vacDto.experience?.name ?: "",
            employment = vacDto.employment?.name ?: "",
            schedule = vacDto.schedule?.name ?: "",
            description = vacDto.description ?: "",
            keySkills = vacDto.keySkills?.map { it.name } ?: emptyList(),
            address = vacDto.address?.raw ?: "",
            geolocation = vacDto.address?.let { map(it) },
            urlHh = vacDto.alternateUrl
        )

    fun map(addressDto: AddressDto): Geolocation =
        Geolocation(
            lat = addressDto.lat?.toString() ?: "",
            lng = addressDto.lng?.toString() ?: ""
        )

    private fun getAreaName(areaDto: AreaDto?): String = areaDto?.name ?: ""
    private fun getEmployerLogo(employerDto: EmployerDto?): String? = employerDto?.logoUrls?.s240

    companion object {
        const val VACANCIES_PER_PAGE = "20"
    }
}
