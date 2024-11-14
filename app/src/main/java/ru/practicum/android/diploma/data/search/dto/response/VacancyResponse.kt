package ru.practicum.android.diploma.data.search.dto.response

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.network.Response
import ru.practicum.android.diploma.data.search.dto.model.VacancyDto

class VacancyResponse(
    val items: ArrayList<VacancyDto>,
    val found: Int? = null,
    val page: Int? = null,
    @SerializedName("per_page") val perPage: Int? = null,
) : Response()
