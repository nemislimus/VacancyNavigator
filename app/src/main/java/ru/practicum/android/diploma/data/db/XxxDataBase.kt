package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.DB_VERSION
import ru.practicum.android.diploma.data.db.dao.AreasDao
import ru.practicum.android.diploma.data.db.dao.CreateDbDao
import ru.practicum.android.diploma.data.db.dao.IndustriesDao
import ru.practicum.android.diploma.data.db.dao.SearchFilterDao
import ru.practicum.android.diploma.data.db.models.AreaRoom
import ru.practicum.android.diploma.data.db.models.AreaRoomTemp
import ru.practicum.android.diploma.data.db.models.IndustryRoom
import ru.practicum.android.diploma.data.db.models.IndustryRoomTemp
import ru.practicum.android.diploma.data.db.models.SearchFilterRoom

@Database(
    version = DB_VERSION,
    entities = [
        AreaRoom::class,
        AreaRoomTemp::class,
        IndustryRoom::class,
        IndustryRoomTemp::class,
        SearchFilterRoom::class
    ]
)
abstract class XxxDataBase : RoomDatabase() {
    abstract fun createDb(): CreateDbDao
    abstract fun areasDao(): AreasDao
    abstract fun industriesDao(): IndustriesDao
    abstract fun searchFilterDao(): SearchFilterDao
}
