package ru.practicum.android.diploma.ui.filtration.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltrationBinding
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationData
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationViewModel
import ru.practicum.android.diploma.ui.utils.BindingFragment
import ru.practicum.android.diploma.util.NumDeclension

open class FiltrationFragment : BindingFragment<FragmentFiltrationBinding>(), NumDeclension {

    private val vModel: FiltrationViewModel by viewModel()
    private var lastNormalSalary = 0
    private var detektHelper: FiltrationFragmentUiDetektHelper? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFiltrationBinding {
        return FragmentFiltrationBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detektHelper = FiltrationFragmentUiDetektHelper(requireContext(), binding)
        detektHelper?.onViewCreated()
        detektHelper?.onResume()
        manageFilterElementClick()
        bindingNumberOne()
        bindingNumberTwo()

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
                        etSalaryEditText.setText(rubFormat(salary.toString()))
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
                detektHelper?.valuesThemeColor?.let { clCountryValue.tvValue.setTextColor(it) }
            } else {
                clCountryValue.tvHint.isVisible = false
            }
        }
    }

    private fun showIndustryValue(filtrationData: FiltrationData.Filter) {
        with(binding) {
            filtrationData.filter.industry?.let { industry ->
                detektHelper?.setIndustryFieldValueUi(industry.name)
            } ?: run {
                detektHelper?.setIndustryFieldValueUi(null)
            }
            with(binding.clIndustryValue) {
                ivElementButton.isVisible = filtrationData.filter.industry == null
                ivClearElementButton.isVisible = filtrationData.filter.industry != null
            }
        }
    }

    private fun bindingNumberOne() {
        with(binding) {
            etSalaryEditText.addTextChangedListener { s ->
                showClearSalaryIcon(s.isNullOrBlank())

                try {
                    val salary: Long = longFromString(s.toString())

                    if (salary < Integer.MAX_VALUE) {
                        lastNormalSalary = salary.toInt()

                        vModel.saveSalary(
                            salary = lastNormalSalary
                        )
                    } else {
                        etSalaryEditText.setText(lastNormalSalary.toString())

                        vModel.showHighSalaryInfo(salary)
                    }
                } catch (er: NumberFormatException) {
                    Log.d("WWW", "High salary $er")
                    etSalaryEditText.setText("0")
                }
            }
        }
    }

    private fun bindingNumberTwo() {
        with(binding) {
            etSalaryEditText.setOnFocusChangeListener { _, hasFocus ->
                detektHelper?.manageSalaryHintColor(hasFocus)
                formatSalary(hasFocus)
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
            clCountryValue.tvValue.setOnClickListener {
                findNavController().navigate(R.id.action_filtrationFragment_to_filtrationPlaceOfWorkFragment)
            }
            clCountryValue.ivElementButton.setOnClickListener {
                findNavController().navigate(R.id.action_filtrationFragment_to_filtrationPlaceOfWorkFragment)
            }

            clIndustryValue.tvValue.setOnClickListener {
                findNavController().navigate(
                    R.id.action_filtrationFragment_to_filtrationIndustryFragment
                )
            }
            clIndustryValue.ivElementButton.setOnClickListener {
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

    private fun rubFormat(salary: String): String {
        val rubString = threeZeroFormat(salary)
        return rubString + if (rubString.isNotBlank()) {
            " " + requireContext().getString(R.string.rub)
        } else {
            ""
        }
    }

    private fun manageSalaryHintColor(hasFocus: Boolean) {
        if (hasFocus) {
            binding.tvSalaryHint.setTextColor(requireContext().getColor(R.color.blue))
        } else {
            if (binding.etSalaryEditText.text.isNullOrBlank()) {
                detektHelper?.salaryThemeColor?.let { binding.tvSalaryHint.setTextColor(it) }
            } else {
                binding.tvSalaryHint.setTextColor(requireContext().getColor(R.color.black))
            }
        }
    }

    private fun formatSalary(hasFocus: Boolean) {
        with(binding) {
            etSalaryEditText.let {
                it.setText(
                    if (!hasFocus) {
                        rubFormat(it.text.toString())
                    } else {
                        it.text.toString().replace(
                            Regex("""[^0-9 ]"""),
                            ""
                        ).trim()
                    }
                )
            }
        }
    }

    companion object {
        const val RESULT_IS_FILTER_APPLIED_KEY = "IS_FILTER_APPLIED_KEY"
    }
}
