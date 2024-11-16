package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.filter.impl.FilterRepositoryImpl
import ru.practicum.android.diploma.data.search.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.domain.filter.api.FilterRepository
import ru.practicum.android.diploma.domain.search.api.SearchRepository

val repositoryModule = module {
    single<FilterRepository> { FilterRepositoryImpl(dataBase = get()) }
    single<SearchRepository> { SearchRepositoryImpl(networkClient = get()) }
}
