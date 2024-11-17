package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.vacancy.viewmodels.VacancyViewModel
import ru.practicum.android.diploma.viewmodels.RootActivityViewModel

val viewModelModule = module {
    viewModel {
        RootActivityViewModel()
    }

    viewModel { (vacancyId: String) ->
        VacancyViewModel(vacancyId, get())
    }
}
