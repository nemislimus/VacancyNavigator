package ru.practicum.android.diploma.data.search.dto

import com.google.gson.annotations.SerializedName

data class VacancyDetailedDto(
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
)
