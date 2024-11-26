package ru.practicum.android.diploma.ui.filtration.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltrationBinding
import ru.practicum.android.diploma.domain.models.SearchFilter
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationData
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationViewModel
import ru.practicum.android.diploma.ui.utils.BindingFragment
import ru.practicum.android.diploma.util.NumDeclension

open class FiltrationFragment : BindingFragment<FragmentFiltrationBinding>(), NumDeclension {

    private val vModel: FiltrationViewModel by viewModel()

    private var salaryThemeColor = 0
    private var valuesThemeColor = 0
    private var lastNormalSalary = 0

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFiltrationBinding {
        return FragmentFiltrationBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAttrColors()
        setFilterFieldUiValues()
        manageFilterElementClick()
        addBindings()

        vModel.getLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is FiltrationData.Filter -> {
                    with(binding) {
                        clCountryValue.tvValue.text = concatAreasNames(it.filter)

                        it.filter.industry?.let { industry ->
                            clIndustryValue.tvValue.text = industry.name
                        } ?: run {
                            clIndustryValue.tvValue.text = requireContext().getText(R.string.industry)
                        }

                        with(binding.clIndustryValue) {
                            ivElementButton.isVisible = it.filter.industry == null
                            ivClearElementButton.isVisible = it.filter.industry != null
                        }

                        it.filter.salary?.let { salary ->
                            etSalaryEditText.setText(
                                rubFormat(salary.toString())
                            )
                        } ?: run {
                            etSalaryEditText.text.clear()
                        }

                        ckbSalaryCheckbox.isChecked = it.filter.onlyWithSalary
                    }
                }

                is FiltrationData.GoBack -> {
                    if (it.applyBeforeExiting) {
                        setFragmentResult(
                            RESULT_IS_FILTER_APPLIED_KEY,
                            bundleOf(RESULT_IS_FILTER_APPLIED_KEY to "true")
                        )
                    }
                    findNavController().navigateUp()
                }

                is FiltrationData.IsFilterChanged -> {
                    with(binding) {
                        btnApplyFilter.isVisible = it.isChanged
                        btnResetFilter.isVisible = it.isChanged
                    }
                }
            }
        }
    }

    private fun addBindings() {
        with(binding) {
            ivEditSalaryBackground.setOnClickListener {
                etSalaryEditText.requestFocus()
            }

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

            etSalaryEditText.setOnFocusChangeListener { _, hasFocus ->
                manageSalaryHintColor(hasFocus)
                if (!hasFocus) closeKeyboard()
                formatSalary(hasFocus)
            }

            tbFilterToolBar.setOnClickListener {
                vModel.goBack(
                    applyBeforeExiting = false
                )
            }

            ivClearSalary.setOnClickListener {
                clearSalary()
            }

            ckbSalaryCheckbox.setOnClickListener {
                etSalaryEditText.clearFocus()
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

    private fun concatAreasNames(filter: SearchFilter): String {
        val names = mutableListOf<String>()
        filter.let {
            if (it.country != null) names.add(it.country.name)
            if (it.region != null) names.add(it.region.name)
            if (it.city != null) names.add(it.city.name)
        }
        with(binding.clCountryValue) {
            ivElementButton.isVisible = names.size == 0
            ivClearElementButton.isVisible = names.size != 0
        }
        if (names.size == 0) {
            return requireContext().getText(R.string.place_of_work).toString()
        }
        return names.joinToString(", ")
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

    private fun getAttrColors() {
        val themeValuesTextColor = TypedValue()
        val themeSalaryHintTextColor = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.elementColor_black_white, themeValuesTextColor, true)
        requireContext().theme.resolveAttribute(R.attr.elementColor_gray_white, themeSalaryHintTextColor, true)
        valuesThemeColor = themeValuesTextColor.data
        salaryThemeColor = themeSalaryHintTextColor.data
    }

    private fun setFilterFieldUiValues() {
        with(binding) {
            clCountryValue.tvValue.text = requireContext().getText(R.string.place_of_work)
            clCountryValue.tvValue.setTextColor(requireContext().getColor(R.color.gray))
            clCountryValue.tvHint.text = requireContext().getText(R.string.place_of_work)

            clIndustryValue.tvValue.text = requireContext().getText(R.string.industry)
            clIndustryValue.tvValue.setTextColor(requireContext().getColor(R.color.gray))
            clIndustryValue.tvHint.text = requireContext().getText(R.string.industry)
        }
    }

    private fun clearSalary() {
        binding.etSalaryEditText.text.clear()
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

    private fun closeKeyboard() {
        activity?.let {
            it.currentFocus?.let { view ->
                val manager = requireActivity().baseContext.getSystemService(
                    INPUT_METHOD_SERVICE
                ) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    view.windowToken,
                    0
                )
            }
        }
    }

    private fun rubFormat(salary: String): String {
        return threeZeroFormat(salary) + " " + requireContext().getString(R.string.rub)
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
        const val EMPTY_STRING = ""
    }
}
