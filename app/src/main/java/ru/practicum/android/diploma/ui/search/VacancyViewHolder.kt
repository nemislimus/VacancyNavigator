package ru.practicum.android.diploma.ui.search

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.domain.search.model.Vacancy

class VacancyViewHolder(
    private val binding: VacancyListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Vacancy) {
        with(binding) {
            tvVacancyName.text = model.name
            tvEmployer.text = model.employer
            tvSalary.text = model.salary
        }
    }
}
