package ru.practicum.android.diploma.data.search.dto.model

import com.google.gson.annotations.SerializedName

data class EmployerDto(
    val id: String,
    val name: String,
    @SerializedName("logo_urls") val logoUrls: LogoUrls? = null,
)

data class LogoUrls(
    @SerializedName("90") val s90: String? = null,
    @SerializedName("240") val s240: String? = null,
    val original: String? = null,
)
