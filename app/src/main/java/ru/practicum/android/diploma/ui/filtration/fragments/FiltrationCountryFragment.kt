package ru.practicum.android.diploma.ui.filtration.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.bundle.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltrationCountriesBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.filtration.adapters.RegionAdapter
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationCountriesViewModel
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationCountryData
import ru.practicum.android.diploma.ui.utils.BindingFragment

class FiltrationCountryFragment : BindingFragment<FragmentFiltrationCountriesBinding>() {

    private val viewModel: FiltrationCountriesViewModel by viewModel()

    private val listAdapter = RegionAdapter { countrySelected(it) }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFiltrationCountriesBinding {
        return FragmentFiltrationCountriesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCountryList.adapter = listAdapter

        binding.tbCountryWorkToolBar.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.liveData.observe(viewLifecycleOwner) { countries ->
            manageCountriesData(countries)
        }
    }

    private fun manageCountriesData(data: FiltrationCountryData) {
        when (data) {
            is FiltrationCountryData.Countries -> setCountries(data.countries)
            is FiltrationCountryData.NotFound -> showPlaceholder()
        }
    }

    private fun setCountries(countries: List<Area>) {
        binding.pbProgressCountry.isVisible = false
        binding.clPlaceholder.root.isVisible = false
        binding.rvCountryList.isVisible = true
        listAdapter.areas.addAll(countries)
        listAdapter.notifyDataSetChanged()
    }

    private fun showPlaceholder() {
        binding.pbProgressCountry.isVisible = false
        binding.rvCountryList.isVisible = false
        with(binding.clPlaceholder) {
            tvPlaceholderText.text = requireContext().getText(R.string.not_found_regions)
            ivPlaceholderPicture.setImageResource(R.drawable.placeholder_filter_region_list_not_found)
            root.isVisible = true
        }
    }

    private fun countrySelected(area: Area) {
        setFragmentResult(RESULT_COUNTRY_KEY, bundleOf(RESULT_COUNTRY_KEY to Gson().toJson(area)))
        findNavController().navigateUp()
    }

    companion object {
        const val RESULT_COUNTRY_KEY = "selected_country"
    }
}
