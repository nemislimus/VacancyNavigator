package ru.practicum.android.diploma.ui.filtration.fragments

import android.content.Context
import android.util.TypedValue
import androidx.core.view.isVisible
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltrationBinding
import ru.practicum.android.diploma.domain.models.SearchFilter

class FiltrationFragmentUiDetektHelper(
    private val context: Context,
    private val binding: FragmentFiltrationBinding
) {
    private var salaryThemeColor = 0
    private var valuesThemeColor = 0

    fun onViewCreated() {
        addBindings()
    }

    fun onResume() {
        getAttrColors()
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

    fun concatAreasNames(filter: SearchFilter): String {
        val names = mutableListOf<String>()
        filter.let {
            if (it.country != null) names.add(it.country.name)
            if (it.region != null) names.add(it.region.name)
            if (it.city != null) names.add(it.city.name)
        }
        with(binding.clCountryValue) {
            ivElementButton.isVisible = names.size == 0
            ivClearElementButton.isVisible = names.size != 0
            tvHint.isVisible = true
        }
        if (names.size == 0) {
            return context.getText(R.string.place_of_work).toString()
        }
        return names.joinToString(", ")
    }

    fun manageSalaryHintColor(hasFocus: Boolean) {
        if (hasFocus) {
            binding.tvSalaryHint.setTextColor(context.getColor(R.color.blue))
        } else {
            if (binding.etSalaryEditText.text.isNullOrBlank()) {
                binding.tvSalaryHint.setTextColor(salaryThemeColor)
            } else {
                binding.tvSalaryHint.setTextColor(context.getColor(R.color.black))
            }
        }
    }

    private fun getAttrColors() {
        val themeValuesTextColor = TypedValue()
        val themeSalaryHintTextColor = TypedValue()
        context.theme.resolveAttribute(R.attr.elementColor_black_white, themeValuesTextColor, true)
        context.theme.resolveAttribute(R.attr.elementColor_gray_white, themeSalaryHintTextColor, true)
        valuesThemeColor = themeValuesTextColor.data
        salaryThemeColor = themeSalaryHintTextColor.data
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
