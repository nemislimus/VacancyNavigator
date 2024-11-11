package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.XxxDataBase

val dataModule = module {

    single<XxxDataBase> {
        Room.databaseBuilder(androidContext(), XxxDataBase::class.java, "xxx-team.db").build()
    }
}
