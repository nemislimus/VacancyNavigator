package ru.practicum.android.diploma.domain.search.model

class SearchVacancyOptions(
    var text: String,
    var page: Int = 0,
    var areaId: String? = null,
    var industryId: String? = null,
    var salary: Int? = null,
    var onlyWithSalary: Boolean? = null,
)
