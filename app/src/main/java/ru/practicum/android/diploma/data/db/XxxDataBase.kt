package ru.practicum.android.diploma.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.practicum.android.diploma.data.db.dao.SomeTableDao
import ru.practicum.android.diploma.data.db.models.SomeItem

@Database(
    version = 1, entities = [
        SomeItem::class
    ]
)
abstract class XxxDataBase : RoomDatabase() {
    abstract fun getDao(): SomeTableDao
}
