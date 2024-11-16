package ru.practicum.android.diploma.ui.search

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.VacancyShort

class VacancyViewHolder(
    private val binding: VacancyListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: VacancyShort) {
        with(binding) {
            tvVacancyName.text = model.name
            tvEmployerName.text = model.employer
            tvSalary.text = Salary.getStringSalaryValue(model.salary)
        }
    }
}
