package ru.practicum.android.diploma.data.search.dto.model

data class VacancyDto(
    val id: String,
    val name: String,
    val salary: SalaryDto? = null,
    val employer: EmployerDto? = null,
    val area: AreaDto? = null,
)
