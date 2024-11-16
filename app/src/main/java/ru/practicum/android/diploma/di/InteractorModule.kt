package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.impl.AreasInteractorImpl
import ru.practicum.android.diploma.domain.impl.IndustriesInteractorImpl
import ru.practicum.android.diploma.domain.impl.UpdateDbOnAppStartUseCase
import ru.practicum.android.diploma.domain.repository.AreasInteractor
import ru.practicum.android.diploma.domain.repository.IndustriesInteractor

val interactorModule = module {
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
}
