package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ru.practicum.android.diploma.data.db.models.SomeItem

@Dao
interface SomeTableDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: SomeItem)
}
