package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.ui.utils.MenuBindingFragment
import ru.practicum.android.diploma.ui.vacancy.models.VacancyDetailsState
import ru.practicum.android.diploma.ui.vacancy.viewmodels.VacancyViewModel

class VacancyFragment : MenuBindingFragment<FragmentVacancyBinding>() {
    override fun getToolbarPanel(): Toolbar = binding.tbVacancyToolBar
    override fun getMenuRes(): Int = R.menu.vacancy_details_menu

    private val viewModel by viewModel<VacancyViewModel> {
        parametersOf(requireArguments().getString(VACANCY_ID_KEY))
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentVacancyBinding {
        return FragmentVacancyBinding.inflate(inflater, container, false)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.miVacancyToFavorite) onFavoriteIconClick()
        if (menuItem.itemId == R.id.miShareVacancy) viewModel.clickOnShareIcon()
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbVacancyToolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            render(state)
        }

        viewModel.observeIsFavorite().observe(viewLifecycleOwner) { isFavorite ->
            setFavIcon(isFavorite)
        }
    }

    private fun render(state: VacancyDetailsState) {
        when (state) {
            is VacancyDetailsState.Loading -> showLoading()
            is VacancyDetailsState.Content -> showContent(state.vacancy)
            is VacancyDetailsState.EmptyResult -> showPlaceholder(state.emptyMessage)
            is VacancyDetailsState.ServerError -> showPlaceholder(state.errorMessage)
            is VacancyDetailsState.NoConnection -> showPlaceholder(state.errorMessage)
        }
    }

    private fun showContent(vacancy: VacancyFull) {
        with(binding) {
            svInfoGroup.isVisible = true
            pbVacancyProgress.isVisible = false
            clPlaceholder.root.isVisible = false

            tvVacancyNameValue.text = vacancy.name
            tvSalaryValue.text = Salary.getStringSalaryValue(vacancy.salary)
            tvEmployerNameValue.text = vacancy.employer
            tvEmployerAreaValue.text = vacancy.address ?: vacancy.areaName
            tvExperienceValue.text = vacancy.experience
            tvEmploymentValue.text = vacancy.employment
            tvVacancyDescriptionValue.text = Html.fromHtml(vacancy.description, Html.FROM_HTML_MODE_LEGACY)

            if (vacancy.keySkills.isEmpty()) {
                tvVacancyKeySkills.isVisible = false
                tvVacancyKeySkillsValue.isVisible = false
            } else {
                tvVacancyKeySkillsValue.text =
                    Html.fromHtml(VacancyFull.keySkillsToHtml(vacancy), Html.FROM_HTML_MODE_LEGACY)
            }
        }

        Glide.with(requireContext())
            .load(vacancy.iconUrl)
            .placeholder(R.drawable.ic_droid)
            .transform(CenterCrop())
            .into(binding.ivEmployerLogoValue)
    }

    private fun showLoading() {
        with(binding) {
            svInfoGroup.isVisible = false
            clPlaceholder.root.isVisible = false
            pbVacancyProgress.isVisible = true
        }
    }

    private fun showPlaceholder(message: String) {
        with(binding) {
            svInfoGroup.isVisible = false
            pbVacancyProgress.isVisible = false
            clPlaceholder.root.isVisible = true
            when (message) {
                requireContext().getString(R.string.vacancy_not_found_or_delete) -> {
                    clPlaceholder.tvPlaceholderText.text = message
                    clPlaceholder.ivPlaceholderPicture.setImageResource(
                        R.drawable.placeholder_vacancy_not_found_or_delete
                    )
                    viewModel.removeCurrentVacancy()
                }

                requireContext().getString(R.string.no_internet) -> {
                    clPlaceholder.tvPlaceholderText.text = message
                    clPlaceholder.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_no_internet_picture)
                }

                else -> {
                    clPlaceholder.tvPlaceholderText.text = requireContext().getString(R.string.server_error)
                    clPlaceholder.ivPlaceholderPicture.setImageResource(R.drawable.placeholder_vacancy_server_error)
                }
            }
        }
    }

    private fun onFavoriteIconClick() {
        viewModel.clickOnFavoriteIcon()
    }

    private fun setFavIcon(vacancyIsFavorite: Boolean) {
        binding.tbVacancyToolBar.menu.findItem(R.id.miVacancyToFavorite).setIcon(
            if (vacancyIsFavorite) R.drawable.favorites_on else R.drawable.favorites_off
        )
    }

    companion object {
        const val VACANCY_ID_KEY = "vac_id_key"

        fun createArgs(vacancyId: String): Bundle =
            bundleOf(VACANCY_ID_KEY to vacancyId)
    }
}
