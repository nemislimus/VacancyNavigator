package ru.practicum.android.diploma.data.search.dto.response

import ru.practicum.android.diploma.data.network.Response
import ru.practicum.android.diploma.data.search.dto.model.VacancyDto

class VacancyResponse (val result: List<VacancyDto>) : Response()
