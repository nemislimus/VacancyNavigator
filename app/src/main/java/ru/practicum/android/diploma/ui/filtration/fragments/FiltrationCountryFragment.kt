package ru.practicum.android.diploma.ui.filtration.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
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

    private val listAdapter = RegionAdapter { area ->
        viewModel.setArea(area)
    }

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
            is FiltrationCountryData.NotFoundCountries -> showPlaceholder(NOT_FOUND_TYPE)
            is FiltrationCountryData.Loading -> showLoading()
            is FiltrationCountryData.NoInternet -> showPlaceholder(NO_INTERNET_TYPE)
            FiltrationCountryData.GoBack -> {
                findNavController().navigateUp()
            }
        }
    }

    private fun setCountries(countries: List<Area>) {
        binding.pbProgressCountry.isVisible = false
        binding.clPlaceholder.root.isVisible = false
        binding.rvCountryList.isVisible = true
        listAdapter.areas.addAll(countries)
        listAdapter.notifyDataSetChanged()
    }

    private fun showPlaceholder(placeholderType: Int) {
        binding.pbProgressCountry.isVisible = false
        binding.rvCountryList.isVisible = false
        with(binding.clPlaceholder) {
            tvPlaceholderText.text = requireContext().getString(
                when (placeholderType) {
                    NO_INTERNET_TYPE -> R.string.no_internet
                    else -> R.string.not_found_list
                }
            )
            ivPlaceholderPicture.setImageResource(
                when (placeholderType) {
                    NO_INTERNET_TYPE -> R.drawable.placeholder_no_internet_picture
                    else -> R.drawable.placeholder_filter_region_list_not_found
                }
            )
            root.isVisible = true
        }
    }

    private fun showLoading() {
        with(binding) {
            clPlaceholder.root.isVisible = false
            rvCountryList.isVisible = false
            pbProgressCountry.isVisible = true
        }
    }

    companion object {
        const val NO_INTERNET_TYPE = 0
        const val NOT_FOUND_TYPE = 1
    }
}
