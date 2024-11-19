package ru.practicum.android.diploma.ui.favorites.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.ui.favorites.viewmodels.FavoritesState
import ru.practicum.android.diploma.ui.favorites.viewmodels.FavoritesViewModel
import ru.practicum.android.diploma.ui.search.rv.VacancyListAdapter
import ru.practicum.android.diploma.ui.utils.BindingFragment
import ru.practicum.android.diploma.ui.vacancy.VacancyFragment

class FavoritesFragment : BindingFragment<FragmentFavoritesBinding>() {

    private val listAdapter = VacancyListAdapter { clickOnVacancy(it) }

    private val viewModel: FavoritesViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = listAdapter

        viewModel.getLiveData().observe(viewLifecycleOwner) {
            when (it) {
                FavoritesState.DbFail -> onDbError()
                is FavoritesState.Vacancies -> {
                    if (it.list.isNotEmpty()) {
                        ifFullList(it.list)
                    } else {
                        ifEmptyList()
                    }
                }
            }
        }
    }

    private fun ifFullList(list: List<VacancyShort>) {
        showElement(binding.recyclerView)
        listAdapter.submitList(list)
    }

    private fun onDbError() {
        showElement(binding.clDbErrorState)
        listAdapter.submitList(emptyList())
    }

    private fun ifEmptyList() {
        showElement(binding.clEmptyState)
        listAdapter.submitList(emptyList())
    }

    private fun showElement(visibleElement: View) {
        val views: List<View> = listOf(
            binding.clEmptyState,
            binding.clDbErrorState,
            binding.recyclerView
        )

        views.forEach {
            it.isVisible = it == visibleElement
        }
    }

    private fun clickOnVacancy(vacancy: VacancyShort) {
        findNavController().navigate(
            R.id.action_favoritesFragment_to_vacancyFragment,
            VacancyFragment.createArgs(vacancy.id)
        )
    }
}
