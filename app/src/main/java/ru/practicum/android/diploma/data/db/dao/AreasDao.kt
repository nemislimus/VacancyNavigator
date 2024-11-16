package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ru.practicum.android.diploma.data.db.models.AreaRoom
import ru.practicum.android.diploma.domain.models.AreaType

@Dao
interface AreasDao {
    @Query("SELECT * FROM areas WHERE type=:type")
    suspend fun getAllAreasByType(type: String): List<AreaRoom>

    @Query("SELECT * FROM areas WHERE type=:type AND name LIKE '%' || :search || '%' ORDER BY parentId")
    suspend fun getAllAreasByTypeAndName(type: String, search: String): List<AreaRoom>


    @Query("SELECT * FROM areas WHERE type=:type")
    suspend fun getAllRegions(type: String): List<AreaRoom>

    @Query("SELECT * FROM areas WHERE type=:type AND name LIKE '%' || :search || '%' ORDER BY parentId")
    suspend fun getAllRegionsByName(type: String, search: String): List<AreaRoom>
}
