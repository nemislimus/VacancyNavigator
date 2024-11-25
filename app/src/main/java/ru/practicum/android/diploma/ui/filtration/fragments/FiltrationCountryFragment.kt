package ru.practicum.android.diploma.ui.filtration.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.databinding.FragmentFiltrationSelectBinding
import ru.practicum.android.diploma.ui.utils.BindingFragment

class FiltrationCountryFragment : BindingFragment<FragmentFiltrationSelectBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentFiltrationSelectBinding {
        return FragmentFiltrationSelectBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tbCountryWorkToolBar.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // Раскоментировать при работе. Закомментриовал из-за detekt
//    private fun countrySelected(area: Area) {
//        setFragmentResult(RESULT_COUNTRY_KEY, bundleOf(RESULT_COUNTRY_KEY to Gson().toJson(area)))
//    }

    companion object {
        const val RESULT_COUNTRY_KEY = "selected_country"
    }
}
