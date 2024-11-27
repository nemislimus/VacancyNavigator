package ru.practicum.android.diploma.ui.filtration.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FilterElementBinding
import ru.practicum.android.diploma.databinding.FragmentFiltrationPlaceOfWorkBinding
import ru.practicum.android.diploma.domain.models.SearchFilter
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationPlaceOfWorkData
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationPlaceOfWorkViewModel
import ru.practicum.android.diploma.ui.utils.BindingFragment

class FiltrationPlaceOfWorkFragment : BindingFragment<FragmentFiltrationPlaceOfWorkBinding>() {

    private val viewModel: FiltrationPlaceOfWorkViewModel by viewModel()

    private var valuesThemeColor = 0

    private var filter: SearchFilter = SearchFilter()

    private var oldFilter: SearchFilter = SearchFilter()

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
            lifecycleScope.launch(Dispatchers.Main) {
                viewModel.resetTempValue()
                findNavController().navigateUp()
            }
        }

        binding.btnApplyPlaceWork.setOnClickListener {
            viewModel.confirmWorkplace()
        }

        viewModel.liveData().observe(viewLifecycleOwner) {
            when (it) {
                FiltrationPlaceOfWorkData.GoBack -> {
                    findNavController().popBackStack()
                }

                is FiltrationPlaceOfWorkData.NewFilter -> {
                    filter = it.filter
                    fillWorkplaceData(filter)
                    binding.btnApplyPlaceWork.isVisible = filter != oldFilter
                }

                is FiltrationPlaceOfWorkData.OldFilter -> {
                    oldFilter = it.filter
                }
            }
        }
    }

    private fun manageFilterElementClick() {
        with(binding) {
            clCountryValue.ivElementButton.setOnClickListener { countryStartSelection() }
            clCountryValue.ivClearElementButton.setOnClickListener { viewModel.setTempArea(null) }
            clRegionValue.ivElementButton.setOnClickListener { regionStartSelection() }
            clRegionValue.ivClearElementButton.setOnClickListener {
                viewModel.setTempArea(
                    area = filter.country
                )
            }
            clCityValue.ivElementButton.setOnClickListener { cityStartSelection() }
            clCityValue.ivClearElementButton.setOnClickListener {
                viewModel.setTempArea(
                    area = filter.region ?: filter.country
                )
            }
        }
    }

    private fun setFilterFieldUiValues() {
        with(binding) {
            clCountryValue.tvHint.text = requireContext().getText(R.string.country)
            clRegionValue.tvHint.text = requireContext().getText(R.string.region)
            clCityValue.tvHint.text = requireContext().getText(R.string.city)
            valuesThemeColor = clCountryValue.tvValue.currentTextColor
        }
        showEmptyCountry()
        showEmptyRegion()
        showEmptyCity()
    }

    private fun countryStartSelection() {
        findNavController().navigate(R.id.action_filtrationPlaceOfWorkFragment_to_filtrationCountryFragment)
    }

    private fun regionStartSelection() {
        findNavController().navigate(
            R.id.action_filtrationPlaceOfWorkFragment_to_filtrationRegionFragment,
            filter.country?.let {
                FiltrationRegionFragment.createArgs(it)
            }
        )
    }

    private fun cityStartSelection() {
        findNavController().navigate(
            R.id.action_filtrationPlaceOfWorkFragment_to_filtrationCityFragment,
            if (filter.region != null) {
                filter.region?.let {
                    FiltrationRegionFragment.createArgs(it)
                }
            } else {
                filter.country?.let {
                    FiltrationRegionFragment.createArgs(it)
                }
            }
        )
    }

    private fun fillWorkplaceData(workPlace: SearchFilter) {
        workPlace.country?.let {
            showFillElement(binding.clCountryValue, it.name)
        } ?: showEmptyCountry()
        workPlace.region?.let {
            showFillElement(binding.clRegionValue, it.name)
        } ?: showEmptyRegion()
        workPlace.city?.let {
            showFillElement(binding.clCityValue, it.name)
        } ?: showEmptyCity()
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

    private fun showEmptyCity() {
        with(binding) {
            clCityValue.tvValue.text = requireContext().getText(R.string.city)
            showEmptyElement(clCityValue)
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
}
