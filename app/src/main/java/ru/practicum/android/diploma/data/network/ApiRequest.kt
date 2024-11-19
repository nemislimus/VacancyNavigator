package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.domain.search.model.SearchVacancyOptions

sealed interface ApiRequest {
    /**
     * Получить списка вакансий
     * @param searchOptions - [SearchVacancyOptions] параметры поиска
     */
    data class Vacancy(val searchOptions: SearchVacancyOptions) : ApiRequest

    /**
     * Получить детали вакансии
     * @param vacancyId - [String] идентификатор вакансии]
     * */
    data class VacancyDetail(val vacancyId: String) : ApiRequest

    /**
     * Получить список отраслей
     * */
    data object Industry : ApiRequest

    /**
     * Получить список стран
     * */
    data object Country : ApiRequest

    /**
     * Получить список подчиненных областей по родителю
     * @param parentAreaId - [String] идентификатор родителя
     * */
    data class Area(val parentAreaId: String) : ApiRequest

    /**
     * Получить список всех областей
     * */
    data object AreasAll : ApiRequest
}
