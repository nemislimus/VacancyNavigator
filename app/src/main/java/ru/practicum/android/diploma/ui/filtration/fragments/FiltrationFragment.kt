package ru.practicum.android.diploma.ui.filtration.fragments

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltrationBinding
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationData
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationViewModel
import ru.practicum.android.diploma.ui.utils.DetektBindingFragment

open class FiltrationFragment : DetektBindingFragment() {

    private val vModel: FiltrationViewModel by viewModel()
    private var lastNormalSalary = 0

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
        manageFilterElementClick()
        bindingNumberOne()
        bindingNumberTwo()
        getAttrColors()

        vModel.getLiveData().observe(viewLifecycleOwner) { renderFiltrationData(it) }
    }

    private fun renderFiltrationData(filtrationData: FiltrationData) {
        when (filtrationData) {
            is FiltrationData.Filter -> {
                with(binding) {
                    clCountryValue.tvValue.text = detektHelper?.concatAreasNames(filtrationData.filter)
                    showCountryValue()
                    showIndustryValue(filtrationData)
                    filtrationData.filter.salary?.let { salary ->
                        etSalaryEditText.setText(salary.toString())
                    } ?: run {
                        etSalaryEditText.text.clear()
                    }
                    ckbSalaryCheckbox.isChecked = filtrationData.filter.onlyWithSalary
                }
            }

            is FiltrationData.GoBack -> {
                if (filtrationData.applyBeforeExiting) {
                    setFragmentResult(
                        RESULT_IS_FILTER_APPLIED_KEY,
                        bundleOf(RESULT_IS_FILTER_APPLIED_KEY to "true")
                    )
                }
                findNavController().navigateUp()
            }

            is FiltrationData.ApplyButton -> {
                binding.btnApplyFilter.isVisible = filtrationData.visible
            }

            is FiltrationData.ResetButton -> {
                binding.btnResetFilter.isVisible = filtrationData.visible
            }
        }
    }

    private fun showCountryValue() {
        with(binding) {
            if (clCountryValue.tvValue.text != requireContext().getString(R.string.place_of_work)) {
                clCountryValue.tvValue.setTextColor(valuesThemeColor)
            } else {
                clCountryValue.tvHint.isVisible = false
            }
        }
    }

    private fun showIndustryValue(filtrationData: FiltrationData.Filter) {
        with(binding) {
            filtrationData.filter.industry?.let { industry ->
                setIndustryFieldValueUi(industry.name)
            } ?: run {
                setIndustryFieldValueUi(null)
            }
            with(binding.clIndustryValue) {
                ivElementButton.isVisible = filtrationData.filter.industry == null
                ivClearElementButton.isVisible = filtrationData.filter.industry != null
            }
        }
    }

    private fun bindingNumberOne() {
        binding.etSalaryEditText.addTextChangedListener(textWatcher)
    }

    override fun onTextInput(text: String) {
        showClearSalaryIcon(text.isBlank())

        try {
            val salary: Long = longFromString(text)

            if (salary < Integer.MAX_VALUE) {
                lastNormalSalary = salary.toInt()

                vModel.saveSalary(
                    salary = lastNormalSalary
                )
            } else {
                binding.etSalaryEditText.setText(lastNormalSalary.toString())

                vModel.showHighSalaryInfo(salary)
            }
        } catch (er: NumberFormatException) {
            Log.d("WWW", "High salary $er")
            binding.etSalaryEditText.setText("0")
        }
    }

    override fun onDestroyFragment() {
        binding.etSalaryEditText.removeTextChangedListener(textWatcher)
    }

    private fun bindingNumberTwo() {
        with(binding) {
            etSalaryEditText.setOnFocusChangeListener { _, hasFocus ->
                detektHelper?.manageSalaryHintColor(hasFocus)
                manageSalaryHintColor(hasFocus)
            }

            tbFilterToolBar.setOnClickListener {
                vModel.goBack(
                    applyBeforeExiting = false
                )
            }

            ckbSalaryCheckbox.setOnClickListener {
                vModel.setOnlyWithSalary(
                    withSalary = ckbSalaryCheckbox.isChecked
                )
            }

            btnApplyFilter.setOnClickListener {
                vModel.goBack(
                    applyBeforeExiting = true
                )
            }

            btnResetFilter.setOnClickListener {
                vModel.resetFilter()
            }
        }
    }

    private fun manageFilterElementClick() {
        with(binding) {
            clCountryValue.wholeElement.setOnClickListener {
                findNavController().navigate(R.id.action_filtrationFragment_to_filtrationPlaceOfWorkFragment)
            }

            clIndustryValue.wholeElement.setOnClickListener {
                findNavController().navigate(
                    R.id.action_filtrationFragment_to_filtrationIndustryFragment
                )
            }
            clIndustryValue.ivClearElementButton.setOnClickListener {
                with(binding.clIndustryValue) {
                    tvHint.isVisible = false
                    ivElementButton.isVisible = true
                    ivClearElementButton.isVisible = false
                    tvValue.setTextColor(requireContext().getColor(R.color.gray))
                }
                vModel.resetIndustry()
            }
            clCountryValue.ivClearElementButton.setOnClickListener {
                with(binding.clCountryValue) {
                    tvHint.isVisible = false
                    ivElementButton.isVisible = true
                    ivClearElementButton.isVisible = false
                    tvValue.setTextColor(requireContext().getColor(R.color.gray))
                }
                vModel.resetWorkplace()
            }
        }
    }

    private fun showClearSalaryIcon(salaryIsEmpty: Boolean) {
        with(binding) {
            ivClearSalary.isVisible = !salaryIsEmpty
        }
    }

    private fun setIndustryFieldValueUi(value: String?) {
        with(binding.clIndustryValue) {
            if (value != null) {
                tvHint.text = requireContext().getText(R.string.industry)
                tvValue.text = value
                tvHint.isVisible = true
                tvValue.setTextColor(valuesThemeColor)
            } else {
                tvHint.isVisible = false
                tvValue.text = requireContext().getText(R.string.industry)
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

    companion object {
        const val RESULT_IS_FILTER_APPLIED_KEY = "IS_FILTER_APPLIED_KEY"
    }
}
