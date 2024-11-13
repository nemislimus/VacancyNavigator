package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.domain.models.SearchFilter

sealed interface ApiRequest {
    data class Vacancy(val text: String, val page: Int = 0, val options: SearchFilter? = null) : ApiRequest
    data class VacancyDetail(val vacancyId: String) : ApiRequest
    data class Industry(val some: Int = 0) : ApiRequest
    data class Country(val some: Int = 0) : ApiRequest
    data class Area(val parentAreaId: String) : ApiRequest
    data class AreasAll(val some: Int = 0) : ApiRequest
}
