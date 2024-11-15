package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.models.AreaRoomTemp
import ru.practicum.android.diploma.data.db.models.IndustryRoomTemp

@Dao
interface OnStartUpdateDao {

    @Query("DELETE FROM areas_temp")
    suspend fun clearTempTable()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArea(item: AreaRoomTemp)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIndustry(item:IndustryRoomTemp)
}
