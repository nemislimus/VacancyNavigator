package ru.practicum.android.diploma.data.search.dto.model

data class AddressDto(
    val id: String,
    val city: String? = null,
    val street: String? = null,
    val building: String? = null,
    val lat: Float? = null,
    val lng: Float? = null,
    val raw: String? = null,
)
