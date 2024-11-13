package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.domain.search.model.SearchVacancyOptions

sealed interface ApiRequest {
    data class Vacancy(val searchOptions: SearchVacancyOptions) : ApiRequest
    data class VacancyDetail(val vacancyId: String) : ApiRequest
    data object Industry : ApiRequest
    data object Country : ApiRequest
    data class Area(val parentAreaId: String) : ApiRequest
    data object AreasAll : ApiRequest
}
