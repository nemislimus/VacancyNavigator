package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.UpdateDbOnAppStartImpl
import ru.practicum.android.diploma.domain.repository.UpdateDbOnAppStart

val repositoryModule = module {
    factory<UpdateDbOnAppStart> {
        UpdateDbOnAppStartImpl(
            client = get(),
            room = get(),
            sql = get()
        )
    }
}
