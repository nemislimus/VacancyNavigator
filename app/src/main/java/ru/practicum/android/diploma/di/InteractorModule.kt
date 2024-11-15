package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.impl.UpdateDbOnAppStartUseCase

val interactorModule = module {
    factory<UpdateDbOnAppStartUseCase> {
        UpdateDbOnAppStartUseCase(
            repository = get()
        )
    }
}
