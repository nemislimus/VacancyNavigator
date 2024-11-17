package ru.practicum.android.diploma.ui.vacancy.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.ui.vacancy.models.VacancyDetailsState

class VacancyViewModel(
    private val vacancyId: String,
    private val context: Context,
) : ViewModel() {

    private val mockVacancy1 = VacancyFull(
        id = "564646",
        name = "Android developer",
        employer = "RandomSoftware",
        areaName = "Москва",
        iconUrl = "https://img.hhcdn.ru/employer-logo/856596.png",
        salary = Salary(80000, 120000, "RUB"),
        experience = "От 1 года до 3 лет",
        employment = "Полная занятость",
        schedule = "Полный день",
        description = "<p>Мы компания с большим портфолио успешных проектов в области HoReCa (ночные бары). С 2003 года мы открыли более 60 заведений в России и Европе.</p> <p>Сейчас нам требуется программист.</p> <p><strong>Основные обязанности:</strong></p> <ul> <li> <p>Написание и автоматизация отчетов (БД + Google Sheets).</p> </li> <li> <p>Разработка/доработка прикладного ПО (по потребностям это Python - серверная часть + БД, немного JS, т.к. есть взаимодействие с Google SpreadSheets).</p> </li> <li> <p>Администрирование БД (MSSQL, MySQL).</p> </li> </ul> <p><strong>Требования:</strong></p> <p>Backend:</p> <ul> <li>node.js</li> <li>python</li> <li>express.js</li> <li>mongoose</li> <li>Опыт работы с любой реляционной БД (SQL), умение писать запросы средней сложности (join, group и так далее).</li> </ul> <p>Frontend:</p> <ul> <li>Vue.js</li> <li>Vuex</li> </ul> <p><strong>Мы готовы предложить вам:</strong></p> <ul> <li> <p>График работы 5/2, с 9:00 до 18:00 или 10:00 до 19:00.</p> </li> <li> <p>ЗП оклад 80000 руб.</p> </li> <li> <p>Выплата зарплаты 2 раза в месяц без задержек.</p> </li> </ul>",
        keySkills = listOf("Базы данных", "Информационные технологии", "Git"),
    )

    private var vacancyDetailsStateLiveData =
        MutableLiveData<VacancyDetailsState>()

    fun observeState(): LiveData<VacancyDetailsState> = vacancyDetailsStateLiveData

    init {
        getVacancyDetails()
    }

    private fun getVacancyDetails() {
        viewModelScope.launch {
            updateState(VacancyDetailsState.Loading)
            delay(2000)
            updateState(VacancyDetailsState.Content(mockVacancy1))
        }
//        updateState(VacancyDetailsState.Content(mockVacancy1))
//        updateState(VacancyDetailsState.Loading)
//        updateState(VacancyDetailsState.EmptyResult(context.getString(R.string.vacancy_not_found_or_delete)))
//        updateState(VacancyDetailsState.ServerError(context.getString(R.string.server_error)))
    }

    private fun updateState(state: VacancyDetailsState) {
        vacancyDetailsStateLiveData.postValue(state)
    }
}
