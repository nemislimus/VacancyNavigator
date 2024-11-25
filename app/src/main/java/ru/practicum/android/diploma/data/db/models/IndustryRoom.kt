package ru.practicum.android.diploma.data.db.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "industry",
    indices = [
        Index(value = ["parentId"])
    ]
)
data class IndustryRoom(
    @PrimaryKey val id: String,
    val name: String,
    val parentId: Int
)
