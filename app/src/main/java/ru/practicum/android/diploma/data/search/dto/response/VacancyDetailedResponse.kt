package ru.practicum.android.diploma.data.search.dto.response

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.network.Response
import ru.practicum.android.diploma.data.search.dto.model.AreaDto
import ru.practicum.android.diploma.data.search.dto.model.EmployerDto
import ru.practicum.android.diploma.data.search.dto.model.EmploymentDto
import ru.practicum.android.diploma.data.search.dto.model.ExperienceDto
import ru.practicum.android.diploma.data.search.dto.model.SalaryDto
import ru.practicum.android.diploma.data.search.dto.model.ScheduleDto
import ru.practicum.android.diploma.data.search.dto.model.SkillDto

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
) : Response()
