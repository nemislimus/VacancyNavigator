package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.Geolocation
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.SearchFilter
import ru.practicum.android.diploma.domain.repository.SearchFilterInteractor
import ru.practicum.android.diploma.domain.repository.SearchFilterRepository

class SearchFilterInteractorImpl(private val repository: SearchFilterRepository) : SearchFilterInteractor {
    override suspend fun getFilter(): Flow<SearchFilter?> {
        return repository.getFilter()
    }

    override suspend fun isFilterExists(): Flow<Boolean> {
        return repository.isFilterExists()
    }

    override suspend fun getFilterForNetworkClient(page: Int): SearchFilter? {
        return repository.getFilterForNetworkClient(page)
    }

    override suspend fun saveCountry(country: Area?) {
        repository.saveCountry(country)
    }

    override suspend fun saveRegion(region: Area?) {
        repository.saveRegion(region)
    }

    override suspend fun saveCity(city: Area?) {
        repository.saveCity(city)
    }

    override suspend fun saveIndustry(industry: Industry?) {
        TODO("Not yet implemented")
    }

    override suspend fun saveSalary(salary: Int?) {
        repository.saveSalary(salary)
    }

    override suspend fun saveOnlyWithSalary(onlyWithSalary: Boolean) {
        repository.saveOnlyWithSalary(onlyWithSalary)
    }

    override suspend fun saveGeolocation(geolocation: Geolocation?) {
        repository.saveGeolocation(geolocation)
    }

    override suspend fun resetFilter() {
        repository.resetFilter()
    }
}
