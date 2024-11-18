package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.models.FavoriteVacancyRoom

@Dao
interface FavoriteVacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(vacancy: FavoriteVacancyRoom)

    @Query("DELETE FROM favorite_vacancy WHERE id=:id")
    suspend fun remove(id: Int)

    @Query("SELECT * FROM favorite_vacancy ORDER BY lastMod DESC")
    fun getList(): Flow<List<FavoriteVacancyRoom>>

    @Query("SELECT * FROM favorite_vacancy WHERE id=:id")
    suspend fun getById(id: Int): FavoriteVacancyRoom?
}
