package ru.practicum.android.diploma.domain.search.model

import ru.practicum.android.diploma.domain.models.SearchFilter

class SearchVacancyOptions(
    var text: String,
    var page: Int = 0,
) {
    val filter: SearchFilter = SearchFilter()
}
