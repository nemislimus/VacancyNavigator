package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import ru.practicum.android.diploma.data.db.models.AreaRoom

@Dao
interface AreasDao {
    @Query("SELECT * FROM areas WHERE nestingLevel=0 ORDER BY hhPosition")
    suspend fun figmaCountries(): List<AreaRoom>

    @Query("SELECT * FROM areas WHERE type='region' ORDER BY name LIMIT $AREAS_PER_PAGE OFFSET :offset")
    suspend fun figmaRegions(offset: Int): List<AreaRoom>

    @Query(
        "SELECT * FROM areas WHERE type='region' AND name LIKE '% ' || :search || '%'" +
            " ORDER BY name LIMIT $AREAS_PER_PAGE OFFSET :offset"
    )
    suspend fun figmaRegionsByName(search: String, offset: Int): List<AreaRoom>

    @Query("SELECT * FROM areas WHERE type=:type ORDER BY name LIMIT $AREAS_PER_PAGE OFFSET :offset")
    suspend fun areasByType(type: String, offset: Int): List<AreaRoom>

    @Query(
        "SELECT * FROM areas WHERE type=:type AND name LIKE '% ' || :search || '%'" +
            " ORDER BY name LIMIT $AREAS_PER_PAGE OFFSET :offset"
    )
    suspend fun areasByTypeAndName(type: String, search: String, offset: Int): List<AreaRoom>

    @Query("SELECT * FROM areas WHERE parentId=:parentId ORDER BY name LIMIT $AREAS_PER_PAGE OFFSET :offset")
    suspend fun areasByParent(parentId: Int, offset: Int): List<AreaRoom>

    @Query(
        "SELECT * FROM areas WHERE parentId=:parentId " +
            "AND name LIKE '% ' || :search || '%' ORDER BY name LIMIT $AREAS_PER_PAGE OFFSET :offset"
    )
    suspend fun areasByParentAndName(parentId: Int, search: String, offset: Int): List<AreaRoom>

    @Query(
        "SELECT c.* FROM areas c INNER JOIN areas r ON c.parentId=r.id " +
            "WHERE r.parentId=:countryId ORDER BY c.name LIMIT $AREAS_PER_PAGE OFFSET :offset"
    )
    suspend fun citiesInCountry(countryId: Int, offset: Int): List<AreaRoom>

    @Query(
        "SELECT c.* FROM areas c INNER JOIN areas r ON c.parentId=r.id WHERE r.parentId=:countryId " +
            "AND c.name LIKE '% ' || :search || '%' ORDER BY c.name LIMIT $AREAS_PER_PAGE OFFSET :offset"
    )
    suspend fun citiesInCountryByName(countryId: Int, search: String, offset: Int): List<AreaRoom>

    @Query("SELECT * FROM areas WHERE id=:areaId")
    suspend fun getAreaById(areaId: Int): AreaRoom?

    companion object {
        const val AREAS_PER_PAGE = 100
    }
}
