package ru.practicum.android.diploma.data.dto

data class VacancyDto (
    val id:String,
    val name:String,
    val salary:SalaryDto? = null

)
