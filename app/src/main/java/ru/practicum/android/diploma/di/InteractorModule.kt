package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.details.api.VacancyDetailsInteractor
import ru.practicum.android.diploma.domain.details.impl.VacancyDetailsInteractorImpl
import ru.practicum.android.diploma.domain.impl.AreasInteractorImpl
import ru.practicum.android.diploma.domain.impl.FavoriteVacancyInteractorImpl
import ru.practicum.android.diploma.domain.impl.FirebaseInteractorImpl
import ru.practicum.android.diploma.domain.impl.GetSearchFilterInteractorImpl
import ru.practicum.android.diploma.domain.impl.IndustriesInteractorImpl
import ru.practicum.android.diploma.domain.impl.SetSearchFilterInteractorImpl
import ru.practicum.android.diploma.domain.impl.UpdateDbOnAppStartUseCase
import ru.practicum.android.diploma.domain.repository.AreasInteractor
import ru.practicum.android.diploma.domain.repository.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.repository.FirebaseInteractor
import ru.practicum.android.diploma.domain.repository.GetSearchFilterInteractor
import ru.practicum.android.diploma.domain.repository.IndustriesInteractor
import ru.practicum.android.diploma.domain.repository.SetSearchFilterInteractor
import ru.practicum.android.diploma.domain.search.api.SearchInteractor
import ru.practicum.android.diploma.domain.search.impl.SearchInteractorImpl

val interactorModule = module {
    single<SearchInteractor> { SearchInteractorImpl(vacancyRepository = get(), filterRepository = get()) }

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

    factory<GetSearchFilterInteractor> {
        GetSearchFilterInteractorImpl(repository = get())
    }

    factory<SetSearchFilterInteractor> {
        SetSearchFilterInteractorImpl(repository = get())
    }

    factory<FavoriteVacancyInteractor> {
        FavoriteVacancyInteractorImpl(
            repository = get()
        )
    }

    factory<FirebaseInteractor> {
        FirebaseInteractorImpl(
            repository = get()
        )
    }

    factory<VacancyDetailsInteractor> {
        VacancyDetailsInteractorImpl(
            vacancyRepository = get()
        )
    }
}
