package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.ui.utils.MenuBindingFragment

class VacancyFragment : MenuBindingFragment<FragmentVacancyBinding>() {
    override fun getToolbarPanel(): Toolbar = binding.tbVacancyToolBar
    override fun getMenuRes(): Int = R.menu.vacancy_details_menu

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentVacancyBinding {
        return FragmentVacancyBinding.inflate(inflater, container, false)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbVacancyToolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUiValues(vacancy: VacancyFull) {
        Glide.with(requireContext())
            .load(vacancy.iconUrl)
            .placeholder(R.drawable.ic_droid)
            .transform(CenterCrop())
            .into(binding.ivEmployerLogoValue)

        if (vacancy.keySkills.isEmpty()) {
            binding.tvVacancyKeySkills.isVisible = false
            binding.tvVacancyKeySkillsValue.isVisible = false
        } else {
            binding.tvVacancyKeySkillsValue.text =
                Html.fromHtml(VacancyFull.keySkillsToHtml(vacancy), Html.FROM_HTML_MODE_LEGACY)
        }

        with(binding) {
            tvVacancyNameValue.text = vacancy.name
            tvSalaryValue.text = Salary.getStringSalaryValue(vacancy.salary)
            tvEmployerNameValue.text = vacancy.employer
            tvEmployerAreaValue.text = vacancy.areaName
            tvExperienceValue.text = vacancy.experience
            tvEmploymentValue.text = vacancy.employment
            tvVacancyDescriptionValue.text = Html.fromHtml(vacancy.description, Html.FROM_HTML_MODE_LEGACY)
        }
    }
}
