package ru.practicum.android.diploma.ui.filtration.fragments

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltrationBinding
import ru.practicum.android.diploma.domain.models.SearchFilter

class FiltrationFragmentUiDetektHelper(
    private val context: Context,
    private val binding: FragmentFiltrationBinding
) {
    var salaryThemeColor = 0
    var valuesThemeColor = 0

    fun onViewCreated() {
        addBindings()
    }

    fun onResume() {
        setFilterFieldUiValues()
    }

    fun addBindings() {
        with(binding) {
            ivEditSalaryBackground.setOnClickListener {
                etSalaryEditText.requestFocus()
            }

            ivClearSalary.setOnClickListener {
                clearSalary()
            }
        }
    }

    fun concatAreasNames(filter: SearchFilter): String? {
        val names = mutableListOf<String>()
        filter.let {
            if (it.country != null) names.add(it.country.name)
            if (it.region != null) names.add(it.region.name)
            if (it.city != null) names.add(it.city.name)
        }

        if (names.size == 0) {
            return null
        }
        return names.joinToString(", ")
    }

    private fun clearSalary() {
        binding.etSalaryEditText.text.clear()
        if (!binding.etSalaryEditText.isFocused) binding.tvSalaryHint.setTextColor(salaryThemeColor)
    }

    private fun setFilterFieldUiValues() {
        with(binding) {
            clCountryValue.tvValue.text = context.getText(R.string.place_of_work)
            clCountryValue.tvValue.setTextColor(context.getColor(R.color.gray))
            clCountryValue.tvHint.text = context.getText(R.string.place_of_work)

            clIndustryValue.tvValue.text = context.getText(R.string.industry)
            clIndustryValue.tvValue.setTextColor(context.getColor(R.color.gray))
            clIndustryValue.tvHint.text = context.getText(R.string.industry)
        }
    }
}
