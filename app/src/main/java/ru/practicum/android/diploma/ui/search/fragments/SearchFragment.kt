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
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.ui.search.rv.VacancyListAdapter
import ru.practicum.android.diploma.ui.search.viewmodels.SearchState
import ru.practicum.android.diploma.ui.search.viewmodels.SearchViewModel
import ru.practicum.android.diploma.ui.utils.MenuBindingFragment
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import ru.practicum.android.diploma.util.EMPTY_STRING

class SearchFragment : MenuBindingFragment<FragmentSearchBinding>() {

    private val listAdapter = VacancyListAdapter { clickOnVacancy(it) }

    private val viewModel: SearchViewModel by viewModel()

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
        binding.rvVacancyList.adapter = listAdapter
        binding.llSearchFieldContainer.ivClearIcon.setOnClickListener {
            clearQuery()
        }

        binding.llSearchFieldContainer.etSearchQueryText.addTextChangedListener { s ->
            if (s.isNullOrBlank()) {
                clearScreen()
            }
            viewModel.searchDebounce(s.toString())
            setSearchIcon(s.isNullOrBlank())
            showIntro(s.isNullOrBlank())
        }

        viewModel.searchState.observe(viewLifecycleOwner) { searchResult ->
            when (searchResult) {
                SearchState.ConnectionError -> showConnectionError()
                is SearchState.Content -> showContent(searchResult.pageData)
                SearchState.IsLoading -> showLoading()
                SearchState.NotFoundError -> showNotFoundError()
            }
        }

    }

    private fun showErrorBase() {
        with(binding) {
            pbSearchProgress.isVisible = false
            rvVacancyList.isVisible = false
            clPlaceholder.root.isVisible = true
        }
    }

    private fun showNotFoundError() {
        showErrorBase()
        with(binding) {
            clPlaceholder.tvPlaceholderText.text = getString(R.string.not_found_vacancies)
            clPlaceholder.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_not_found_picture)
        }
    }

    private fun showConnectionError() {
        showErrorBase()
        with(binding) {
            clPlaceholder.tvPlaceholderText.text = getString(R.string.no_internet)
            clPlaceholder.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_no_internet_picture)
        }
    }

    private fun clearScreen() {
        listAdapter.submitList(emptyList())
        clearPlaceholders()
    }

    private fun clearPlaceholders() {
        with(binding) {
            clPlaceholder.root.isVisible = false
            pbSearchProgress.isVisible = false
        }
    }

    private fun showLoading() {
        with(binding) {
            pbSearchProgress.isVisible = true
        }
    }

    private fun showContent(searchData: List<VacancyShort>) {
        binding.rvVacancyList.isVisible = true
        clearPlaceholders()
        listAdapter.submitList(searchData)
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
        with(binding) {
            llSearchFieldContainer.etSearchQueryText.setText(EMPTY_STRING)
            rvVacancyList.isVisible = false
            pbSearchProgress.isVisible = false
        }
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
