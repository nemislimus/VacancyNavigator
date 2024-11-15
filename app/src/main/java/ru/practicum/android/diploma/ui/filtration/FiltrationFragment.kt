package ru.practicum.android.diploma.ui.filtration

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltrationBinding
import ru.practicum.android.diploma.ui.utils.BindingFragment
import ru.practicum.android.diploma.util.EMPTY_STRING

open class FiltrationFragment : BindingFragment<FragmentFiltrationBinding>() {

    private var salaryThemeColor = 0
    private var valuesThemeColor = 0

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFiltrationBinding {
        return FragmentFiltrationBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAttrColors()

        with(binding) {
            clCountryValue.tvValue.text = requireContext().getText(R.string.place_of_work)
            clCountryValue.tvHint.text = requireContext().getText(R.string.place_of_work)

            clIndustryValue.tvValue.text = requireContext().getText(R.string.industry)
            clIndustryValue.tvHint.text = requireContext().getText(R.string.industry)
        }

        manageFilterElementClick()

        binding.etSalaryEditText.addTextChangedListener { s ->
            showClearSalaryIcon(s.isNullOrBlank())
        }

        binding.etSalaryEditText.setOnFocusChangeListener { _, hasFocus ->
            manageSalaryHintColor(hasFocus)
        }

        binding.tbFilterToolBar.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ivClearSalary.setOnClickListener {
            clearSalary()
        }

        binding.ckbSalaryCheckbox.setOnClickListener {
            binding.etSalaryEditText.clearFocus()
        }
    }

    private fun manageFilterElementClick() {
        binding.clCountryValue.ivElementButton.setOnClickListener {
            with(binding.clCountryValue) {
                tvHint.isVisible = true
                ivElementButton.isVisible = false
                ivClearElementButton.isVisible = true
                tvValue.setTextColor(valuesThemeColor)
            }
        }

        binding.clIndustryValue.ivElementButton.setOnClickListener {
            with(binding.clIndustryValue) {
                tvHint.isVisible = true
                ivElementButton.isVisible = false
                ivClearElementButton.isVisible = true
                tvValue.setTextColor(valuesThemeColor)
            }
        }

        binding.clCountryValue.ivClearElementButton.setOnClickListener {
            with(binding.clCountryValue) {
                tvHint.isVisible = false
                ivElementButton.isVisible = true
                ivClearElementButton.isVisible = false
                tvValue.setTextColor(requireContext().getColor(R.color.gray))
            }
        }

        binding.clIndustryValue.ivClearElementButton.setOnClickListener {
            with(binding.clIndustryValue) {
                tvHint.isVisible = false
                ivElementButton.isVisible = true
                ivClearElementButton.isVisible = false
                tvValue.setTextColor(requireContext().getColor(R.color.gray))
            }
        }
    }

    private fun getAttrColors() {
        val themeValuesTextColor = TypedValue()
        val themeSalaryHintTextColor = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.elementColor_black_white, themeValuesTextColor, true)
        requireContext().theme.resolveAttribute(R.attr.elementColor_gray_white, themeSalaryHintTextColor, true)
        valuesThemeColor = themeValuesTextColor.data
        salaryThemeColor = themeSalaryHintTextColor.data
    }

    private fun clearSalary() {
        binding.etSalaryEditText.setText(EMPTY_STRING)
        if (!binding.etSalaryEditText.isFocused) binding.tvSalaryHint.setTextColor(salaryThemeColor)
    }

    private fun showClearSalaryIcon(salaryIsEmpty: Boolean) {
        with(binding) {
            ivClearSalary.isVisible = !salaryIsEmpty
        }
    }

    private fun manageSalaryHintColor(hasFocus: Boolean) {
        if (hasFocus) {
            binding.tvSalaryHint.setTextColor(requireContext().getColor(R.color.blue))
        } else {
            if (binding.etSalaryEditText.text.isNullOrBlank()) {
                binding.tvSalaryHint.setTextColor(salaryThemeColor)
            } else {
                binding.tvSalaryHint.setTextColor(requireContext().getColor(R.color.black))
            }
        }
    }

}
