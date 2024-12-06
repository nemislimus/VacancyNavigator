package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.favorites.viewmodels.FavoritesViewModel
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationCountriesViewModel
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationIndustryViewModel
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationPlaceOfWorkViewModel
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationRegionViewModel
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationViewModel
import ru.practicum.android.diploma.ui.root.viewmodels.RootActivityViewModel
import ru.practicum.android.diploma.ui.search.viewmodels.SearchViewModel
import ru.practicum.android.diploma.ui.vacancy.viewmodels.VacancyViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        RootActivityViewModel(
            firebase = get()
        )
    }

    viewModel { (vacancyId: String) ->
        VacancyViewModel(vacancyId, get(), get(), get(), get())
    }

    viewModel {
        FavoritesViewModel(
            favorites = get()
        )
    }

    viewModel {
        FiltrationIndustryViewModel(
            industriesGetter = get(),
            filterGetter = get(),
            filterSetter = get(),
            loadingStatus = get()
        )
    }

    viewModel { (countryId: String) ->
        FiltrationRegionViewModel(
            regionsGetter = get(),
            filterSetter = get(),
            parentId = countryId,
            loadingStatus = get()
        )
    }

    viewModel {
        FiltrationPlaceOfWorkViewModel(
            filterGetter = get(),
            filterSetter = get()
        )
    }

    viewModel {
        FiltrationCountriesViewModel(
            countryGetter = get(),
            filterSetter = get(),
            loadingStatus = get()
        )
    }

    viewModel {
        FiltrationViewModel(
            filterSetter = get(),
            filterGetter = get(),
            systemInt = get()
        )
    }
}
