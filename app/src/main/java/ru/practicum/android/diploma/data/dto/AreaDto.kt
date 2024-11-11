package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class AreaDto(
    val id: String,
    @SerializedName("parent_id") val parentId: String? = null,
    val name: String,
    val areas: List<AreaDto>? = null,
)
