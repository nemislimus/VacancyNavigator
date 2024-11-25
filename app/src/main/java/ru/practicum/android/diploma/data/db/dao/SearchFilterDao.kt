package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.models.AreaRoom
import ru.practicum.android.diploma.data.db.models.SearchFilterRoom

@Dao
interface SearchFilterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFilter(defaultFilter: SearchFilterRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceFilter(filter: SearchFilterRoom)

    @Query("SELECT * FROM search_filter WHERE filterId=1")
    fun getFilterFlow(): Flow<SearchFilterRoom?>

    @Query("SELECT * FROM search_filter WHERE filterId=1")
    suspend fun getFilter(): SearchFilterRoom?

    @Query("SELECT * FROM areas WHERE id=:id")
    suspend fun getParentArea(id: Int): AreaRoom?

    @Query("DELETE FROM search_filter WHERE filterId=1")
    suspend fun deleteFilter()
}
