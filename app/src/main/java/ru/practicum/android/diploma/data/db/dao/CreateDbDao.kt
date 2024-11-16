package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CreateDbDao {
    @Query("SELECT sqlite_version()")
    fun version(): String
}
