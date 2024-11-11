package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDetailedDto(
    val id: String,
    val name: String,
    val experience: ExperienceDto? = null,
    val schedule: ScheduleDto? = null,
    val employment: EmploymentDto? = null,
    val description: String? = null,
    @SerializedName("branded_description") val brandedDescription: String? = null,
    @SerializedName("key_skills") val keySkills: List<SkillDto>? = null,
)
