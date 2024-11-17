package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.impl.AreasInteractorImpl
import ru.practicum.android.diploma.domain.impl.IndustriesInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchFilterInteractorImpl
import ru.practicum.android.diploma.domain.impl.UpdateDbOnAppStartUseCase
import ru.practicum.android.diploma.domain.repository.AreasInteractor
import ru.practicum.android.diploma.domain.repository.IndustriesInteractor
import ru.practicum.android.diploma.domain.repository.SearchFilterInteractor
import ru.practicum.android.diploma.domain.search.api.SearchInteractor
import ru.practicum.android.diploma.domain.search.impl.SearchInteractorImpl

val interactorModule = module {
    single<SearchInteractor> { SearchInteractorImpl(searchRepository = get(), filterRepository = get()) }

    factory<UpdateDbOnAppStartUseCase> {
        UpdateDbOnAppStartUseCase(
            repository = get()
        )
    }

    factory<IndustriesInteractor> {
        IndustriesInteractorImpl(
            repository = get()
        )
    }

    factory<AreasInteractor> {
        AreasInteractorImpl(
            repository = get()
        )
    }

    factory<SearchFilterInteractor> {
        SearchFilterInteractorImpl(repository = get())
    }
}
