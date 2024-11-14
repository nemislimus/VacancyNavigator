package ru.practicum.android.diploma.data.search.dto.response

import ru.practicum.android.diploma.data.network.Response
import ru.practicum.android.diploma.data.search.dto.model.CountryDto

class CountryResponse(val result: ArrayList<CountryDto>) : Response()
