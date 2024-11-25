package ru.practicum.android.diploma.ui.filtration.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
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

class FiltrationRegionFragment : BindingFragment<FragmentFiltrationRegionBinding>() {

    private val viewModel: FiltrationRegionViewModel by viewModel {
        val countryId: String? = arguments?.getString(COUNTRY_ID_KEY)
        parametersOf(countryId)
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
                viewModel.getRegions(s.toString())
            }

        }

        viewModel.liveData.observe(viewLifecycleOwner) {
            when (it) {
                FiltrationRegionData.GoBack -> goBack()
                FiltrationRegionData.NotFound -> showNotFound()
                FiltrationRegionData.NotSuchRegion -> {}
                is FiltrationRegionData.Regions -> showRegions(it.regions)
            }
        }

    }

    private fun showRegions(regions: List<Area>) {
        setPlaceholdersVisibility(false)
        binding.rvRegionList.isVisible = true
        listAdapter.submitList(regions)
    }

    private fun setPlaceholdersVisibility(isVisible: Boolean) {
        binding.clPlaceholderRegion.root.isVisible = isVisible
    }

    private fun showNotFound() {
        listAdapter.submitList(emptyList())
        setPlaceholdersVisibility(true)
        with(binding) {
            rvRegionList.isVisible = false
            clPlaceholderRegion.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_filter_region_list_not_found)
            clPlaceholderRegion.tvPlaceholderText.text = requireContext().getText(R.string.not_found_regions)
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
        findNavController().navigateUp()
        // Здесь передать результат предыдущему фрагменту
    }

    companion object {

        const val COUNTRY_ID_KEY = "country id key"

        fun setParams(countryId: String): Bundle {
            return bundleOf(COUNTRY_ID_KEY to countryId)
        }
    }

}
