package ru.practicum.android.diploma.ui.filtration.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FilterElementBinding
import ru.practicum.android.diploma.databinding.FragmentFiltrationPlaceOfWorkBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.filtration.model.WorkPlace
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationPlaceOfWorkState
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationPlaceOfWorkViewModel
import ru.practicum.android.diploma.ui.utils.BindingFragment

class FiltrationPlaceOfWorkFragment : BindingFragment<FragmentFiltrationPlaceOfWorkBinding>() {

    private val viewModel by viewModel<FiltrationPlaceOfWorkViewModel> {
        val area = arguments?.getString(CURRENT_AREA_KEY)?.let { json ->
            Gson().fromJson(json, Area::class.java)
        }
        parametersOf(area)
    }

    private var valuesThemeColor = 0

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFiltrationPlaceOfWorkBinding {
        return FragmentFiltrationPlaceOfWorkBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFilterFieldUiValues()

        manageFilterElementClick()

        binding.tbPlaceWorkToolBar.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnApplyPlaceWork.setOnClickListener { viewModel.confirmWorkplace() }

        viewModel.stateObserver().observe(viewLifecycleOwner) { state ->
            render(state)
        }

    }

    private fun manageFilterElementClick() {
        with(binding) {
            clCountryValue.ivElementButton.setOnClickListener { countryStartSelection() }
            clCountryValue.ivClearElementButton.setOnClickListener { viewModel.countryChange(null) }
            clRegionValue.ivElementButton.setOnClickListener { regionStartSelection() }
            clRegionValue.ivClearElementButton.setOnClickListener { viewModel.regionChange(null) }
        }
    }

    private fun setFilterFieldUiValues() {
        with(binding) {
            clCountryValue.tvHint.text = requireContext().getText(R.string.country)
            clRegionValue.tvHint.text = requireContext().getText(R.string.region)
            valuesThemeColor = clCountryValue.tvValue.currentTextColor
        }
        showEmptyCountry()
        showEmptyRegion()
    }

    private fun countryStartSelection() {
        setFragmentResultListener(FiltrationCountryFragment.RESULT_COUNTRY_KEY) { key, bundle ->
            bundle.getString(FiltrationCountryFragment.RESULT_COUNTRY_KEY)?.apply {
                val country = Gson().fromJson(this, Area::class.java)
                countrySelected(country)
            }
        }
        findNavController().navigate(R.id.action_filtrationPlaceOfWorkFragment_to_filtrationCountryFragment)
    }

    private fun countrySelected(country: Area) {
        viewModel.countryChange(country)
        clearFragmentResultListener(FiltrationCountryFragment.RESULT_COUNTRY_KEY)
    }

    private fun regionStartSelection() {
        setFragmentResultListener(FiltrationRegionFragment.RESULT_REGION_KEY) { key, bundle ->
            bundle.getString(FiltrationRegionFragment.RESULT_REGION_KEY)?.apply {
                val region = Gson().fromJson(this, Area::class.java)
                regionSelected(region)
            }
        }
        findNavController().navigate(
            R.id.action_filtrationPlaceOfWorkFragment_to_filtrationRegionFragment,
            viewModel.getCurrentCountry()?.let {
                FiltrationRegionFragment.createArgs(it)
            }
        )
    }

    private fun regionSelected(region: Area) {
        viewModel.regionChange(region)
        clearFragmentResultListener(FiltrationRegionFragment.RESULT_REGION_KEY)
    }

    private fun render(state: FiltrationPlaceOfWorkState) {
        fillWorkplaceData(state.workPlace)
        when (state) {
            is FiltrationPlaceOfWorkState.Modified -> binding.btnApplyPlaceWork.isVisible = true
            is FiltrationPlaceOfWorkState.Unmodified -> binding.btnApplyPlaceWork.isVisible = false
            is FiltrationPlaceOfWorkState.Confirm -> onConfirm(state.workPlace)
        }
    }

    private fun fillWorkplaceData(workPlace: WorkPlace) {
        workPlace.country?.let {
            showFillElement(binding.clCountryValue, it.name)
        } ?: showEmptyCountry()
        workPlace.region?.let {
            showFillElement(binding.clRegionValue, it.name)
        } ?: showEmptyRegion()
    }

    private fun onConfirm(workPlace: WorkPlace) {
        val result = workPlace.region ?: let { workPlace.country }
        setFragmentResult(PLACE_OF_WORK_RESULT_KEY, bundleOf(PLACE_OF_WORK_RESULT_KEY to Gson().toJson(result)))
        findNavController().popBackStack()
    }

    private fun showEmptyCountry() {
        with(binding) {
            clCountryValue.tvValue.text = requireContext().getText(R.string.country)
            showEmptyElement(clCountryValue)
        }
    }

    private fun showEmptyRegion() {
        with(binding) {
            clRegionValue.tvValue.text = requireContext().getText(R.string.region)
            showEmptyElement(clRegionValue)
        }
    }

    private fun showFillElement(element: FilterElementBinding, text: String) {
        element.tvValue.text = text
        element.tvValue.setTextColor(valuesThemeColor)
        element.ivClearElementButton.isVisible = true
        element.ivElementButton.isVisible = false
        element.tvHint.isVisible = true
    }

    private fun showEmptyElement(element: FilterElementBinding) {
        element.tvValue.setTextColor(requireContext().getColor(R.color.gray))
        element.ivClearElementButton.isVisible = false
        element.ivElementButton.isVisible = true
        element.tvHint.isVisible = false
    }

    companion object {
        const val PLACE_OF_WORK_RESULT_KEY = "place_of_work"
        private const val CURRENT_AREA_KEY = "current_area"
        fun createArgs(area: Area) = bundleOf(CURRENT_AREA_KEY to Gson().toJson(area))
    }

}
