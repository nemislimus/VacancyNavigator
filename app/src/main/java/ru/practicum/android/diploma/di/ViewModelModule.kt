package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.viewmodels.MainActivityViewModel

val viewModelModule = module {
    viewModel {
        MainActivityViewModel(dataUpdater = get())
    }
}
