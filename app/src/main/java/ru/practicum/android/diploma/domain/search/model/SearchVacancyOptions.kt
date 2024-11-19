package ru.practicum.android.diploma.domain.search.model

import ru.practicum.android.diploma.domain.models.SearchFilter

/** Параметры поиска
 * @param text - Наименование вакансии или ещё часть
 * @param page - Страница списка
 * @param filter - Фильтр вакансий [SearchFilter]
 * @author Ячменев И.
 * */
data class SearchVacancyOptions(
    val text: String,
    val page: Int = 0,
    val filter: SearchFilter? = null
)
