package ru.practicum.android.diploma.data.db.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "areas_temp",
    indices = [
        Index(value = ["type"]),
        Index(value = ["parentId"])
    ]
)
data class AreaRoomTemp(
    @PrimaryKey val id: Int,
    val name: String, // Москва, Воронеж, Сочи
    val type: String, // country, region, city
    val parentId: Int
)
