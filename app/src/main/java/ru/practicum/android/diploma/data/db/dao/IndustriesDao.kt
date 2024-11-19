package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import ru.practicum.android.diploma.data.db.models.IndustryRoom

@Dao
interface IndustriesDao {
    @Query("SELECT * FROM industry ORDER BY parentId")
    suspend fun getIndustries(): List<IndustryRoom>

    @Query("SELECT * FROM industry WHERE name LIKE '%' || :search || '%'")
    suspend fun getIndustriesByName(search: String): List<IndustryRoom>
}
