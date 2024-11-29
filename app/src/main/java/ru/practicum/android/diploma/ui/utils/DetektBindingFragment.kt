package ru.practicum.android.diploma.ui.utils

import android.os.Bundle
import android.view.View
import ru.practicum.android.diploma.databinding.FragmentFiltrationBinding
import ru.practicum.android.diploma.ui.filtration.fragments.FiltrationFragmentUiDetektHelper
import ru.practicum.android.diploma.util.NumDeclension

abstract class DetektBindingFragment : BindingFragment<FragmentFiltrationBinding>(), NumDeclension {

    protected var detektHelper: FiltrationFragmentUiDetektHelper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detektHelper = FiltrationFragmentUiDetektHelper(requireContext(), binding)
        detektHelper?.onViewCreated()
        detektHelper?.onResume()
    }

    protected fun rubFormat(salary: String): String {
        val rubString = threeZeroFormat(salary)
        return if (rubString.length <= MAX_SALARY_LENGTH) {
            rubString
        } else {
            salary
        }
    }

    protected fun formatSalary(hasFocus: Boolean) {
        with(binding) {
            etSalaryEditText.let {
                val noSpaceSalary = it.text.toString().replace(
                    Regex("""[^0-9 ]"""),
                    ""
                ).trim()

                if (!hasFocus) {
                    it.setText(
                        rubFormat(noSpaceSalary)
                    )
                }

            }
        }
    }

    companion object {
        const val MAX_SALARY_LENGTH = 9
    }
}
