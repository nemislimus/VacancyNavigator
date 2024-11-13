package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.domain.models.SearchFilter

sealed interface ApiRequest {
    data class Vacancy(val text: String, val page: Int = 0, val options: SearchFilter? = null) : ApiRequest
    data class VacancyDetail(val vacancyId: String) : ApiRequest
    data object Industry : ApiRequest
    data object Country : ApiRequest
    data class Area(val parentAreaId: String) : ApiRequest
    data object AreasAll : ApiRequest
}
