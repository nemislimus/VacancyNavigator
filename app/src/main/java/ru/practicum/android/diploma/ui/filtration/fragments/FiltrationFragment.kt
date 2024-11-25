package ru.practicum.android.diploma.ui.filtration.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltrationBinding
import ru.practicum.android.diploma.domain.models.SearchFilter
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationData
import ru.practicum.android.diploma.ui.filtration.viewmodels.FiltrationViewModel
import ru.practicum.android.diploma.ui.utils.BindingFragment


open class FiltrationFragment : BindingFragment<FragmentFiltrationBinding>() {

    private val vModel: FiltrationViewModel by viewModel()

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
        setFilterFieldUiValues()
        manageFilterElementClick()

        binding.etSalaryEditText.addTextChangedListener { s ->
            showClearSalaryIcon(s.isNullOrBlank())

            vModel.saveSalary(
                salary = s.toString().replace(
                    regex = Regex("""[^0-9]""", RegexOption.IGNORE_CASE),
                    replacement = ""
                ).toInt()
            )
        }

        binding.etSalaryEditText.setOnFocusChangeListener { _, hasFocus ->
            manageSalaryHintColor(hasFocus)
        }

        binding.tbFilterToolBar.setOnClickListener {
            vModel.goBack(
                applyBeforeExiting = false
            )
        }

        binding.ivClearSalary.setOnClickListener {
            clearSalary()
        }

        binding.ckbSalaryCheckbox.setOnClickListener {
            binding.etSalaryEditText.clearFocus()
            vModel.setOnlyWithSalary(
                withSalary = binding.ckbSalaryCheckbox.isChecked
            )
        }

        binding.btnApplyFilter.setOnClickListener {
            vModel.goBack(
                applyBeforeExiting = true
            )
        }

        binding.btnResetFilter.setOnClickListener {
            vModel.resetFilter()
        }

        vModel.getLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is FiltrationData.Filter -> {
                    with(binding) {
                        clCountryValue.tvValue.text = concatAreasNames(it.filter)

                        it.filter.industry?.let { industry ->
                            clIndustryValue.tvValue.text = industry.name
                        }

                        it.filter.salary?.let { salary ->
                            etSalaryEditText.setText(salary.toString())
                        } ?: run {
                            etSalaryEditText.setText("0")
                        }

                        ckbSalaryCheckbox.isChecked = it.filter.onlyWithSalary
                    }
                }

                is FiltrationData.GoBack -> {
                    if (it.applyBeforeExiting) {
                        // apply filter before exiting
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

    private fun concatAreasNames(filter: SearchFilter): String {
        val names = mutableListOf<String>()
        filter.let {
            if (it.country != null) names.add(it.country.name)
            if (it.region != null) names.add(it.region.name)
            if (it.city != null) names.add(it.city.name)
        }
        return names.joinToString(", ")
    }

    private fun manageFilterElementClick() {
        binding.clCountryValue.ivElementButton.setOnClickListener {
            findNavController().navigate(R.id.action_filtrationFragment_to_filtrationPlaceOfWorkFragment)
        }

        binding.clIndustryValue.ivElementButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_filtrationFragment_to_filtrationIndustryFragment
            )
        }

        binding.clCountryValue.ivClearElementButton.setOnClickListener {
            with(binding.clCountryValue) {
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
        binding.etSalaryEditText.setText("0")
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
