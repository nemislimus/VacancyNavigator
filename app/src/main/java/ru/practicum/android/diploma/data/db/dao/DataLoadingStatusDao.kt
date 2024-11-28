package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.models.DataLoadingStatusRoom

@Dao
interface DataLoadingStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: DataLoadingStatusRoom)

    @Query("SELECT * FROM data_loading_status ORDER BY level DESC LIMIT 1")
    fun getStatus(): Flow<DataLoadingStatusRoom?>
}
