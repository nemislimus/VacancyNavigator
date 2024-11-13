package ru.practicum.android.diploma.data.network

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.search.dto.model.AreaDto
import ru.practicum.android.diploma.data.search.dto.model.CountryDto
import ru.practicum.android.diploma.data.search.dto.model.EmployerDto
import ru.practicum.android.diploma.data.search.dto.model.EmploymentDto
import ru.practicum.android.diploma.data.search.dto.model.ExperienceDto
import ru.practicum.android.diploma.data.search.dto.model.IndustryDto
import ru.practicum.android.diploma.data.search.dto.model.SalaryDto
import ru.practicum.android.diploma.data.search.dto.model.ScheduleDto
import ru.practicum.android.diploma.data.search.dto.model.SkillDto
import ru.practicum.android.diploma.data.search.dto.model.VacancyDto

sealed class ApiResponse {
    var resultCode: Int = 0

    data class BadResponse(val message: String = "") : ApiResponse()

    data class VacancyResponse(
        val items: ArrayList<VacancyDto>,
        val found: Int? = null,
        val page: Int? = null,
        @SerializedName("per_page") val perPage: Int? = null,
    ) : ApiResponse()

    data class VacancyDetailedResponse(
        val id: String,
        val name: String,
        val salary: SalaryDto? = null,
        val employer: EmployerDto? = null,
        val area: AreaDto? = null,
        val experience: ExperienceDto? = null,
        val schedule: ScheduleDto? = null,
        val employment: EmploymentDto? = null,
        val description: String? = null,
        @SerializedName("key_skills") val keySkills: List<SkillDto>? = null,
    ) : ApiResponse()

    class IndustryResponse(val result: ArrayList<IndustryDto>) : ApiResponse()

    class CountryResponse(val result: ArrayList<CountryDto>) : ApiResponse()

    class AreaResponse(val areas: ArrayList<AreaDto>) : ApiResponse()

    companion object {
        const val NO_CONNECTION_CODE = -1
        const val SUCCESSFUL_RESPONSE_CODE = 200
        const val INCORRECT_PARAM_ERROR_CODE = 400
        const val CAPTCHA_REQUIRED_ERROR = 403
        const val NOT_FOUND_CODE = 404
        const val INTERNAL_SERV_ERROR_CODE = 500
        const val BAD_GATEWAY_CODE = 502
    }
}
