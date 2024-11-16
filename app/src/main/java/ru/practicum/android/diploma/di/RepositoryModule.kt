package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.AreasRepositoryImpl
import ru.practicum.android.diploma.data.impl.IndustriesRepositoryImpl
import ru.practicum.android.diploma.data.impl.UpdateDbOnAppStartRepositoryImpl
import ru.practicum.android.diploma.domain.repository.AreasRepository
import ru.practicum.android.diploma.domain.repository.IndustriesRepository
import ru.practicum.android.diploma.domain.repository.UpdateDbOnAppStartRepository

val repositoryModule = module {
    factory<UpdateDbOnAppStartRepository> {
        UpdateDbOnAppStartRepositoryImpl(
            client = get(),
            sql = get(),
            roomDb = get()
        )
    }

    factory<IndustriesRepository> {
        IndustriesRepositoryImpl(
            dao = get()
        )
    }

    factory<AreasRepository> {
        AreasRepositoryImpl(
            dao = get()
        )
    }
}
