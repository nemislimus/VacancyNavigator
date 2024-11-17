package ru.practicum.android.diploma.ui.search.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.ui.search.VacancyListAdapter
import ru.practicum.android.diploma.ui.utils.MenuBindingFragment
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import ru.practicum.android.diploma.util.EMPTY_STRING

class SearchFragment : MenuBindingFragment<FragmentSearchBinding>() {

    private val mockVacancyShort = VacancyShort(
        id = "564646",
        name = "Android developer",
        employer = "RandomSoftware",
        areaName = "Москва",
        iconUrl = "https://img.hhcdn.ru/employer-logo/856596.png",
        salary = Salary(null, 120000, "RUB")
    )
    private val listVac = listOf(mockVacancyShort)

    private val listAdapter = VacancyListAdapter { clickOnVacancy(it) }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun getToolbarPanel(): Toolbar {
        return binding.tbSearchToolBar
    }

    override fun getMenuRes(): Int {
        return R.menu.search_toolbar_menu
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        goToFilter()
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter.submitList(listVac)
        binding.rvVacancyList.isVisible = true

        binding.rvVacancyList.adapter = listAdapter

        binding.llSearchFieldContainer.ivClearIcon.setOnClickListener {
            clearQuery()
        }

        binding.llSearchFieldContainer.etSearchQueryText.addTextChangedListener { s ->
            setSearchIcon(s.isNullOrBlank())
            showIntro(s.isNullOrBlank())
        }
    }

    private fun setSearchIcon(queryIsEmpty: Boolean) {
        with(binding.llSearchFieldContainer) {
            ivSearchIcon.isVisible = queryIsEmpty
            ivClearIcon.isVisible = !queryIsEmpty
        }
    }

    private fun showIntro(queryIsEmpty: Boolean) {
        binding.ivIntroPicture.isVisible = queryIsEmpty
    }

    private fun clearQuery() {
        binding.llSearchFieldContainer.etSearchQueryText.setText(EMPTY_STRING)
    }

    private fun goToFilter() {
        findNavController().navigate(
            R.id.action_searchFragment_to_filtrationFragment
        )
    }

    private fun clickOnVacancy(vacancy: VacancyShort) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.id)
        )
    }
}
