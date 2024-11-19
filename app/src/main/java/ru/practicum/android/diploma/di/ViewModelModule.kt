package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.favorites.viewmodels.FavoritesViewModel
import ru.practicum.android.diploma.ui.root.viewmodels.RootActivityViewModel
import ru.practicum.android.diploma.ui.search.viewmodels.SearchViewModel
import ru.practicum.android.diploma.ui.vacancy.viewmodels.VacancyViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        RootActivityViewModel()
    }

    viewModel { (vacancyId: String) ->
        VacancyViewModel(vacancyId, get(), get(), get(), get())
    }

    viewModel {
        FavoritesViewModel(
            favorites = get()
        )
    }
}
