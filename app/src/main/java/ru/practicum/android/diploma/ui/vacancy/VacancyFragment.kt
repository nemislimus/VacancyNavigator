package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
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
}
