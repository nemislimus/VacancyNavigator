package ru.practicum.android.diploma.data.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.search.dto.model.CountryDto
import ru.practicum.android.diploma.data.search.dto.model.IndustryDto
import ru.practicum.android.diploma.data.search.dto.response.VacancyDetailedResponse
import ru.practicum.android.diploma.data.search.dto.model.VacancyDto
import ru.practicum.android.diploma.data.search.dto.response.AreaResponse

interface HhSearchApi {

    @GET("vacancies")
    suspend fun searchVacancies(@QueryMap options: Map<String, String>): ArrayList<VacancyDto>

    @GET("vacancies/{id}")
    suspend fun getVacancyDetails(@Path("id") vacancyId: String): VacancyDetailedResponse

    @GET("areas/countries")
    suspend fun getCountries(): ArrayList<CountryDto>

    @GET("areas/{id}")
    suspend fun getAreasByCountry(@Path("id") countryId: String): AreaResponse

    @GET("industries")
    suspend fun getIndustries(): ArrayList<IndustryDto>

}
