package ru.practicum.android.diploma.ui.filtration.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltrationRegionBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.filtration.adapters.RegionAdapter
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationRegionData
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationRegionViewModel
import ru.practicum.android.diploma.ui.utils.BindingFragment
import ru.practicum.android.diploma.util.EMPTY_STRING
import ru.practicum.android.diploma.util.debounce

open class FiltrationRegionFragment : BindingFragment<FragmentFiltrationRegionBinding>() {

    open val viewModel: FiltrationRegionViewModel by viewModel {
        val countryId: String? = arguments?.getString(COUNTRY_ID_KEY)
        parametersOf(countryId)
    }

    private var lastSearchRequest: String = EMPTY_STRING

    private val _searchDebounce: (String) -> Unit =
        debounce(true, lifecycleScope, SEARCH_DEBOUNCE_DELAY) { searchText ->
            lastSearchRequest = searchText
            viewModel.getRegions(searchText)
        }

    private val listAdapter = RegionAdapter {
        viewModel.saveRegion(it)
    }

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFiltrationRegionBinding {
        return FragmentFiltrationRegionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llSearchRegionField.etSearchRegionQuery.hint = requireContext().getText(R.string.enter_region_query)

        with(binding) {
            tbRegionToolBar.setOnClickListener { goBack() }
            rvRegionList.adapter = listAdapter
            llSearchRegionField.ivClearIcon.setOnClickListener {
                clearQuery()
            }

            llSearchRegionField.etSearchRegionQuery.addTextChangedListener { s ->
                setSearchIcon(s.isNullOrBlank())
                searchRegion(s.toString().trim())
            }
        }

        viewModel.liveData.observe(viewLifecycleOwner) {
            when (it) {
                is FiltrationRegionData.Regions -> showRegions(it.regions)
                is FiltrationRegionData.GoBack -> {
                    it.region?.let { region -> regionSelected(region) }
                    goBack()
                }
                FiltrationRegionData.NotFoundRegion -> showPlaceholder(NOT_FOUND_TYPE)
                FiltrationRegionData.IncorrectRegion -> showPlaceholder(INCORRECT_TYPE)
                FiltrationRegionData.NoInternet -> showPlaceholder(NO_INTERNET_TYPE)
                FiltrationRegionData.Loading -> showLoading()
            }
        }
    }

    private fun searchRegion(searchQuery: String) {
        if (searchQuery == lastSearchRequest) {
            return
        }
        _searchDebounce(searchQuery)
    }

    private fun showRegions(regions: List<Area>) {
        binding.pbRegionProgress.isVisible = false
        binding.clPlaceholderRegion.root.isVisible = false
        binding.rvRegionList.isVisible = true
        showNewList(regions)
    }

//    open fun showPlaceholder(notFoundList: Boolean = false, usingForCities: Boolean = false) {
//        binding.rvRegionList.isVisible = false
//        val placeholderText = if (usingForCities) R.string.city_not_found else R.string.region_not_found
//        with(binding.clPlaceholderRegion) {
//            tvPlaceholderText.text = requireContext().getString(
//                if (notFoundList) R.string.not_found_list else placeholderText
//            )
//            ivPlaceholderPicture.setImageResource(
//                if (notFoundList) {
//                    R.drawable.placeholder_filter_region_list_not_found
//                } else {
//                    R.drawable.placeholder_not_found_picture
//                }
//            )
//            root.isVisible = true
//        }
//    }

    open fun showPlaceholder(placeholderType: Int = NO_INTERNET_TYPE, usingForCities: Boolean = false) {
        binding.pbRegionProgress.isVisible = false
        binding.rvRegionList.isVisible = false
        val placeholderTextIncorrect = if (usingForCities) R.string.city_not_found else R.string.region_not_found
        with(binding.clPlaceholderRegion) {
            tvPlaceholderText.text = requireContext().getString(
                when(placeholderType) {
                    NO_INTERNET_TYPE -> R.string.no_internet
                    NOT_FOUND_TYPE -> R.string.not_found_list
                    else -> placeholderTextIncorrect
                }
            )
            ivPlaceholderPicture.setImageResource(
                when(placeholderType) {
                    NO_INTERNET_TYPE -> R.drawable.placeholder_no_internet_picture
                    NOT_FOUND_TYPE -> R.drawable.placeholder_filter_region_list_not_found
                    else -> R.drawable.placeholder_not_found_picture
                }
            )
            root.isVisible = true
        }
    }

    private fun showLoading() {
        with(binding) {
            clPlaceholderRegion.root.isVisible = false
            rvRegionList.isVisible = false
            pbRegionProgress.isVisible = true
        }
    }

    private fun setSearchIcon(queryIsEmpty: Boolean) {
        with(binding.llSearchRegionField) {
            ivSearchIcon.isVisible = queryIsEmpty
            ivClearIcon.isVisible = !queryIsEmpty
        }
    }

    private fun clearQuery() {
        with(binding) {
            llSearchRegionField.etSearchRegionQuery.setText(EMPTY_STRING)
        }
        closeKeyboard()
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

    private fun goBack() {
        findNavController().popBackStack()
    }

    private fun showNewList(areas: List<Area>? = null) {
        listAdapter.areas.clear()
        areas?.let { list -> listAdapter.areas.addAll(list) }
        listAdapter.notifyDataSetChanged()
    }

    private fun regionSelected(region: Area) {
        setFragmentResult(RESULT_REGION_KEY, bundleOf(RESULT_REGION_KEY to Gson().toJson(region)))
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 200L
        const val RESULT_REGION_KEY = "selected_region_key"
        const val COUNTRY_ID_KEY = "country_id_key"

        const val NO_INTERNET_TYPE = 0
        const val NOT_FOUND_TYPE = 1
        const val INCORRECT_TYPE = 2

        fun createArgs(country: Area): Bundle {
            Log.d("WWW", country.toString())
            return bundleOf(COUNTRY_ID_KEY to country.id)
        }
    }
}
