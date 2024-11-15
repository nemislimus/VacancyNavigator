package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.dao.AreasDao
import ru.practicum.android.diploma.data.db.dao.OnStartUpdateDao
import ru.practicum.android.diploma.data.db.models.AreaRoom
import ru.practicum.android.diploma.data.db.models.AreaRoomTemp
import ru.practicum.android.diploma.data.db.models.IndustryRoom
import ru.practicum.android.diploma.data.db.models.IndustryRoomTemp

@Database(
    version = DB_VERSION,
    entities = [
        AreaRoom::class,
        AreaRoomTemp::class,
        IndustryRoom::class,
        IndustryRoomTemp::class
    ]
)
abstract class XxxDataBase : RoomDatabase() {
    abstract fun areasDao(): AreasDao
    abstract fun dbUpdateDao(): OnStartUpdateDao
}
