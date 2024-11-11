package ru.practicum.android.diploma.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "some_table_to_delete")
data class SomeItem(
    @PrimaryKey val id: Int
)
