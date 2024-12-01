package ru.practicum.android.diploma.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "areas",
    indices = [
        Index(value = ["type"]),
        Index(value = ["parentId"]),
        Index(value = ["nestingLevel"])
    ]
)
data class AreaRoom @JvmOverloads constructor(
    @PrimaryKey val id: Int,
    val name: String, // Москва, Воронеж, Сочи
    val type: String, // country=0, region=1, city=2
    val parentId: Int,
    val nestingLevel: Int, // уровень вложенности в выдаче API hh.ru
    @ColumnInfo(defaultValue = "0")
    val hhPosition: Int // позиция в выдаче hh.ru
)
