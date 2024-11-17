package ru.practicum.android.diploma.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.practicum.android.diploma.util.EMPTY_STRING

@Entity(tableName = "search_filter")
data class SearchFilterRoom @JvmOverloads constructor(
    @PrimaryKey val filterId: Int = 1,
    val countryId: Int = 0,
    val countryName: String = EMPTY_STRING,
    val countryParentId: Int = 0,
    val regionId: Int = 0,
    val regionName: String = EMPTY_STRING,
    val regionParentId: Int = 0,
    val cityId: Int = 0,
    val cityName: String = EMPTY_STRING,
    val cityParentId: Int = 0,
    val industryId: String = EMPTY_STRING,
    val industryName: String = EMPTY_STRING,
    val industryParentId: Int = 0,
    val salary: Int = -1,
    val onlyWithSalary: Int = 0,
    val lat: String = EMPTY_STRING,
    val lng: String = EMPTY_STRING
)
