package ru.practicum.android.diploma.ui.filtration.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltrationPlaceOfWorkBinding
import ru.practicum.android.diploma.ui.utils.BindingFragment

class FiltrationPlaceOfWorkFragment : BindingFragment<FragmentFiltrationPlaceOfWorkBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
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
       with(binding){
           clRegionValue.ivElementButton.setOnClickListener{
               findNavController().navigate(R.id.action_filtrationPlaceOfWorkFragment_to_filtrationRegionFragment)
           }
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

}
