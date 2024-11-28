package ru.practicum.android.diploma.ui.filtration.fragments

import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationCityViewModel

class FiltrationCityFragment : FiltrationRegionFragment() {
    override val viewModel: FiltrationCityViewModel by viewModel {
        val countryId: String? = arguments?.getString(COUNTRY_ID_KEY)
        parametersOf(countryId)
    }

    override fun onResume() {
        super.onResume()
        binding.tbRegionToolBar.setTitle(requireContext().getString(R.string.city_select))
        binding.llSearchRegionField.etSearchRegionQuery.hint = requireContext().getText(R.string.enter_city_query)
    }

    override fun showPlaceholder(notFoundList: Boolean, usingForCities: Boolean) {
        super.showPlaceholder(notFoundList, true)
    }
}
