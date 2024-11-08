package com.example.playlistmaker.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.impl.TeamLogRepositoryImpl
import ru.practicum.android.diploma.domain.repository.TeamLogRepository

val repositoryModule = module {

    factory<TeamLogRepository> {
        TeamLogRepositoryImpl(get())
    }
}
