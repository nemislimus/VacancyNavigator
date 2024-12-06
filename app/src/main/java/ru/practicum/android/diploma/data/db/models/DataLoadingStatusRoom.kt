package ru.practicum.android.diploma.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "data_loading_status"
)
data class DataLoadingStatusRoom(
    @PrimaryKey val level: Int,
    val code: Int,
    val message: String
)
