package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.search.api.SearchInteractor
import ru.practicum.android.diploma.domain.search.impl.SearchInteractorImpl

val interactorModule = module {
    single<SearchInteractor> { SearchInteractorImpl(searchRepository = get(), filterRepository = get()) }
}
