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
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.ui.filtration.fragments.FiltrationFragment
import ru.practicum.android.diploma.ui.search.rv.VacancyListAdapter
import ru.practicum.android.diploma.ui.search.viewmodels.SearchState
import ru.practicum.android.diploma.ui.search.viewmodels.SearchViewModel
import ru.practicum.android.diploma.ui.utils.MenuBindingFragment
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment
import ru.practicum.android.diploma.util.EMPTY_STRING
import ru.practicum.android.diploma.util.NumDeclension

class SearchFragment : MenuBindingFragment<FragmentSearchBinding>(), NumDeclension {

    private val listAdapter = VacancyListAdapter { clickOnVacancy(it) }

    private var hasFilter: Boolean = false

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

            llSearchFieldContainer.etSearchQueryText.addTextChangedListener(textWatcher)

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

        viewModel.searchState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchState.IsLoading -> showLoading()
                SearchState.IsLoadingNextPage -> showLoadingNextPage()
                is SearchState.Content -> showContent(state.pageData, state.listNeedsScrollTop)
                is SearchState.ConnectionError -> showConnectionError(state.replaceVacancyList)
                is SearchState.NotFoundError -> showNotFoundError(state.replaceVacancyList)
                is SearchState.VacanciesCount -> setResultInfo(state.vacanciesCount, R.string.search_result_info)
                is SearchState.ServerError500 -> showServerError500(state.replaceVacancyList)
                is SearchState.QueryIsEmpty -> {
                    if (state.isEmpty) {
                        clearScreen()
                    }
                    showIntro(state.isEmpty)
                    setSearchIcon(state.isEmpty)
                }

                is SearchState.SearchText -> {
                    binding.llSearchFieldContainer.etSearchQueryText.setText(state.text)
                }
            }
        }

        viewModel.filterState.observe(viewLifecycleOwner) { filterState ->
            setFilterIcon(filterState)
            hasFilter = filterState
        }

        binding.root.post {
            setFilterIcon(hasFilter)
        }
    }

    override fun onDestroyFragment() {
        binding.llSearchFieldContainer.etSearchQueryText.removeTextChangedListener(textWatcher)
    }

    override fun onTextInput(text: String) {
        viewModel.searchDebounce(text.trim())
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

    private fun showNotFoundError(replaceVacancyList: Boolean) {
        if (!replaceVacancyList) {
            binding.pbNextPageProgress.isVisible = false
            showToast(R.string.no_query_vacancies)
        } else {
            setResultInfo(messageId = R.string.no_query_vacancies)
            showErrorBase()
            with(binding) {
                clPlaceholder.tvPlaceholderText.text = getString(R.string.not_found_vacancies)
                clPlaceholder.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_not_found_picture)
            }
        }
    }

    private fun showServerError500(replaceVacancyList: Boolean) {
        if (!replaceVacancyList) {
            binding.pbNextPageProgress.isVisible = false
            showToast(R.string.server_error)
        } else {
            setResultInfo(messageId = R.string.server_error)
            showErrorBase()
            with(binding) {
                clPlaceholder.tvPlaceholderText.text = getString(R.string.server_error)
                clPlaceholder.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_search_server_error)
            }
        }
    }

    private fun showConnectionError(replaceVacancyList: Boolean) {
        if (!replaceVacancyList) {
            binding.pbNextPageProgress.isVisible = false
            showToast(R.string.check_internet_connection)
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
        with(binding) {
            rvVacancyList.isVisible = false
            tvResultInfo.isVisible = false
        }
        clearPlaceholders()
        showFoundVacancies()
    }

    private fun clearQuery() {
        binding.llSearchFieldContainer.etSearchQueryText.setText(EMPTY_STRING)
        closeKeyboard()
    }

    private fun goToFilter() {
        setFragmentResultListener(FiltrationFragment.RESULT_IS_FILTER_APPLIED_KEY) { key, bundle ->
            bundle.getString(FiltrationFragment.RESULT_IS_FILTER_APPLIED_KEY)?.apply {
                viewModel.searchAfterFilterApplied()
            }
        }
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

    private fun setFilterIcon(hasFilter: Boolean) {
        binding.tbSearchToolBar.menu.findItem(R.id.miSearchFilter).setIcon(
            if (hasFilter) R.drawable.filter_on else R.drawable.filter_off
        )
    }
}
