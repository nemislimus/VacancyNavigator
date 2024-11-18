package ru.practicum.android.diploma.data.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.network.ApiResponse
import ru.practicum.android.diploma.data.search.dto.model.CountryDto
import ru.practicum.android.diploma.data.search.dto.model.IndustryDto

interface HhSearchApi {

    @GET("vacancies")
    suspend fun searchVacancies(@QueryMap options: Map<String, String>): ApiResponse.VacancyResponse

    @GET("vacancies/{id}")
    suspend fun getVacancyDetails(@Path("id") vacancyId: String): ApiResponse.VacancyDetailedResponse

    @GET("areas/countries")
    suspend fun getCountries(): ArrayList<CountryDto>

    @GET("areas/{id}")
    suspend fun getAreasByCountry(@Path("id") countryId: String): ApiResponse.AreaResponse

    @GET("areas/0")
    suspend fun getAreas(): ApiResponse.AreaResponse

    @GET("industries")
    suspend fun getIndustries(): ArrayList<IndustryDto>
}
