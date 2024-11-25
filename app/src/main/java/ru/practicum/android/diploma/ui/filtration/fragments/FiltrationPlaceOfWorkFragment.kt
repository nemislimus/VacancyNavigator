package ru.practicum.android.diploma.ui.filtration.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltrationPlaceOfWorkBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.utils.BindingFragment

class FiltrationPlaceOfWorkFragment : BindingFragment<FragmentFiltrationPlaceOfWorkBinding>() {

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
    }

    private fun manageFilterElementClick() {
        with(binding) {
            clCountryValue.ivElementButton.setOnClickListener { countryStartSelection() }
            clRegionValue.ivElementButton.setOnClickListener { regionStartSelection() }
        }
    }

    private fun setFilterFieldUiValues() {
        with(binding) {
            clCountryValue.tvValue.text = requireContext().getText(R.string.country)
            clCountryValue.tvValue.setTextColor(requireContext().getColor(R.color.gray))
            clCountryValue.tvHint.text = requireContext().getText(R.string.country)

            clRegionValue.tvValue.text = requireContext().getText(R.string.region)
            clRegionValue.tvValue.setTextColor(requireContext().getColor(R.color.gray))
            clRegionValue.tvHint.text = requireContext().getText(R.string.region)
        }
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
        binding.clCountryValue.tvValue.text = country.name
        binding.clCountryValue.tvValue.isVisible = true
        clearFragmentResultListener(FiltrationCountryFragment.RESULT_COUNTRY_KEY)
    }

    private fun regionStartSelection() {
        setFragmentResultListener(FiltrationRegionFragment.RESULT_REGION_KEY) { key, bundle ->
            bundle.getString(FiltrationRegionFragment.RESULT_REGION_KEY)?.apply {
                val region = Gson().fromJson(this, Area::class.java)
                regionSelected(region)
            }
        }
        findNavController().navigate(R.id.action_filtrationPlaceOfWorkFragment_to_filtrationRegionFragment)
    }

    private fun regionSelected(region: Area) {
        binding.clRegionValue.tvValue.text = region.name
        binding.clRegionValue.tvValue.isVisible = true
        clearFragmentResultListener(FiltrationRegionFragment.RESULT_REGION_KEY)
    }

    companion object {
        const val PLACE_OF_WORK_RESULT_KEY = "place_of_work"
    }

}
