package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import ru.practicum.android.diploma.data.db.models.AreaRoom

@Dao
interface AreasDao {
    @Query("SELECT * FROM areas WHERE nestingLevel=0")
    suspend fun figmaCountries(): List<AreaRoom>

    @Query("SELECT * FROM areas WHERE nestingLevel=1 AND type!='country'")
    suspend fun figmaRegions(): List<AreaRoom>

    @Query("SELECT * FROM areas WHERE nestingLevel=1 AND type!='country' AND name LIKE '%' || :search || '%'")
    suspend fun figmaRegionsByName(search: String): List<AreaRoom>

    @Query("SELECT * FROM areas WHERE type=:type")
    suspend fun areasByType(type: String): List<AreaRoom>

    @Query("SELECT * FROM areas WHERE type=:type AND name LIKE '%' || :search || '%'")
    suspend fun areasByTypeAndName(type: String, search: String): List<AreaRoom>

    @Query("SELECT * FROM areas WHERE parentId=:parentId")
    suspend fun areasByParent(parentId: Int): List<AreaRoom>

    @Query("SELECT * FROM areas WHERE parentId=:parentId AND name LIKE '%' || :search || '%'")
    suspend fun areasByParentAndName(parentId: Int, search: String): List<AreaRoom>

    @Query("SELECT c.* FROM areas c INNER JOIN areas r ON c.parentId=r.id WHERE r.parentId=:countryId")
    suspend fun citiesInCountry(countryId: Int): List<AreaRoom>

    @Query(
        "SELECT c.* FROM areas c INNER JOIN areas r ON c.parentId=r.id WHERE r.parentId=:countryId " +
            "AND c.name LIKE '%' || :search || '%'"
    )
    suspend fun citiesInCountryByName(countryId: Int, search: String): List<AreaRoom>

    @Query("SELECT COUNT(*) FROM areas c INNER JOIN areas r ON c.parentId=r.id WHERE r.parentId=:countryId")
    suspend fun countCitiesInCountry(countryId: Int): Int

    @Query("SELECT COUNT(*) FROM areas WHERE parentId=:parentId")
    suspend fun countAreasInParent(parentId: Int): Int
}
