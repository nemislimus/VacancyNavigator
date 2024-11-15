package ru.practicum.android.diploma.data.db.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "areas",
    indices = [
        Index(value = ["type"]),
        Index(value = ["parentId"])
    ]
)
data class AreaRoom(
    @PrimaryKey val id: Int,
    val name: String, // Москва, Воронеж, Сочи
    val type: String, // country=0, region=1, city=2
    val parentId: Int
)
