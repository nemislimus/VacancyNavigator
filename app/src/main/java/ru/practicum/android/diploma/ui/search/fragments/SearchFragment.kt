package ru.practicum.android.diploma.ui.search.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
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
import ru.practicum.android.diploma.util.declension

class SearchFragment : MenuBindingFragment<FragmentSearchBinding>() {

    private val listAdapter = VacancyListAdapter { clickOnVacancy(it) }

    private val viewModel: SearchViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        listAdapter.setGapONListTop()
        with(binding) {
            rvVacancyList.adapter = listAdapter
            llSearchFieldContainer.ivClearIcon.setOnClickListener {
                clearQuery()
            }

            llSearchFieldContainer.etSearchQueryText.addTextChangedListener { s ->
                if (s.isNullOrBlank()) {
                    clearScreen()
                }
                viewModel.searchDebounce(s.toString())
                setSearchIcon(s.isNullOrBlank())
                showIntro(s.isNullOrBlank())
            }
            rvVacancyList.addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        val pos =
                            (binding.rvVacancyList.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                        if (pos >= listAdapter.itemCount - 1) {
                            viewModel.onLastItemReached()
                        }
                        viewModel.setNoScrollOnViewCreated()
                    }
                }
            })
        }

        viewModel.searchState.observe(viewLifecycleOwner) { searchResult ->
            when (searchResult) {
                SearchState.IsLoading -> showLoading()
                SearchState.IsLoadingNextPage -> showLoadingNextPage()
                is SearchState.Content -> showContent(searchResult.pageData, searchResult.listNeedsScrollTop)
                is SearchState.ConnectionError -> showConnectionError(searchResult.replaceVacancyList)
                SearchState.NotFoundError -> showNotFoundError()
                is SearchState.VacanciesCount -> setResultInfo(searchResult.vacanciesCount, R.string.search_result_info)
            }
        }
    }

    private fun showLoadingNextPage() {
        binding.pbNextPageProgress.isVisible = true
    }

    private fun showErrorBase() {
        with(binding) {
            pbSearchProgress.isVisible = false
            pbNextPageProgress.isVisible = false
            rvVacancyList.isVisible = false
            clPlaceholder.root.isVisible = true
        }
    }

    private fun showToast(message: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showNotFoundError() {
        setResultInfo(messageId = R.string.no_query_vacancies)
        showErrorBase()
        with(binding) {
            clPlaceholder.tvPlaceholderText.text = getString(R.string.not_found_vacancies)
            clPlaceholder.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_not_found_picture)
        }
    }

    private fun showConnectionError(replaceVacancyList: Boolean) {
        if (!replaceVacancyList) {
            binding.pbNextPageProgress.isVisible = false
            showToast(R.string.no_internet)
        } else {
            showErrorBase()
            with(binding) {
                clPlaceholder.tvPlaceholderText.text = getString(R.string.no_internet)
                clPlaceholder.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_no_internet_picture)
                tvResultInfo.isVisible = false
            }
        }
    }

    private fun clearPlaceholders() {
        with(binding) {
            clPlaceholder.root.isVisible = false
            pbSearchProgress.isVisible = false
            pbNextPageProgress.isVisible = false
        }
    }

    private fun showLoading() {
        with(binding) {
            clPlaceholder.root.isVisible = false
            pbSearchProgress.isVisible = true
            tvResultInfo.isVisible = false
        }
    }

    private fun showContent(searchData: List<VacancyShort>, listNeedsScrollTop: Boolean) {
        with(binding) {
            rvVacancyList.isVisible = true
        }
        clearPlaceholders()
        closeKeyboard()

        showFoundVacancies(vacancies = searchData, scrollToTop = listNeedsScrollTop)
    }

    private fun setResultInfo(vacanciesCount: Int = -1, messageId: Int) {
        binding.tvResultInfo.isVisible = true
        if (vacanciesCount != -1) {
            binding.tvResultInfo.text = declension(vacanciesCount, getString(messageId))
        } else {
            binding.tvResultInfo.text = getString(messageId)
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

    private fun clearScreen() {
        showFoundVacancies()
        binding.rvVacancyList.isVisible = false
        binding.tvResultInfo.isVisible = false
        clearPlaceholders()
        viewModel.cancelSearch()
    }

    private fun clearQuery() {
        with(binding) {
            llSearchFieldContainer.etSearchQueryText.setText(EMPTY_STRING)
            rvVacancyList.isVisible = false
            pbSearchProgress.isVisible = false
        }
        showFoundVacancies()
        closeKeyboard()
        viewModel.cancelSearch()
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

    private fun closeKeyboard() {
        activity?.let {
            it.currentFocus?.let { view ->
                val manager = requireActivity().baseContext.getSystemService(
                    INPUT_METHOD_SERVICE
                ) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    view.windowToken,
                    0
                )
            }
        }
    }

    private fun showFoundVacancies(vacancies: List<VacancyShort>? = null, scrollToTop: Boolean = false) {
        listAdapter.vacancies.clear()
        /*
         * searchData.toMutableList() поставлен специально
         * подробности тут https://stackoverflow.com/questions/49726385/listadapter-not-updating-item-in-recyclerview
         * */
        vacancies?.let { list -> listAdapter.vacancies.addAll(list.toMutableList()) }
        listAdapter.notifyDataSetChanged()
        if (scrollToTop) {
            binding.rvVacancyList.scrollToPosition(0)
        }
    }
}
