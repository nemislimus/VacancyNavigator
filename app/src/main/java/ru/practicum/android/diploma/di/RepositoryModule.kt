package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.AreasRepositoryImpl
import ru.practicum.android.diploma.data.impl.GetSearchFilterRepositoryImpl
import ru.practicum.android.diploma.data.impl.IndustriesRepositoryImpl
import ru.practicum.android.diploma.data.impl.SetSearchFilterRepositoryImpl
import ru.practicum.android.diploma.data.impl.UpdateDbOnAppStartRepositoryImpl
import ru.practicum.android.diploma.data.search.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.domain.repository.AreasRepository
import ru.practicum.android.diploma.domain.repository.GetSearchFilterRepository
import ru.practicum.android.diploma.domain.repository.IndustriesRepository
import ru.practicum.android.diploma.domain.repository.SetSearchFilterRepository
import ru.practicum.android.diploma.domain.repository.UpdateDbOnAppStartRepository
import ru.practicum.android.diploma.domain.search.api.SearchRepository

val repositoryModule = module {
    single<GetSearchFilterRepository> {
        GetSearchFilterRepositoryImpl(
            dao = get()
        )
    }

    factory<SetSearchFilterRepository> {
        SetSearchFilterRepositoryImpl(
            dao = get()
        )
    }

    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get())
    }

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
