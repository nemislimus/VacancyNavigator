package com.example.playlistmaker.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.impl.TeamLogInteractorImpl
import ru.practicum.android.diploma.domain.repository.TeamLogInteractor

val interactorModule = module {

    factory<TeamLogInteractor> {
        TeamLogInteractorImpl(get())
    }
}
