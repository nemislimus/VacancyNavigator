package ru.practicum.android.diploma.ui.search.rv

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.VacancyShort

class VacancyViewHolder(
    private val binding: VacancyListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: VacancyShort) {
        Glide.with(binding.ivItemLogo).clear(binding.ivItemLogo)

        with(binding) {
            tvVacancyName.text = combineNameAndArea(model)
            tvEmployerName.text = model.employer
            tvSalary.text = Salary.getStringSalaryValue(model.salary)

            Glide.with(itemView)
                .load(model.iconUrl)
                .placeholder(R.drawable.ic_droid)
                .transform(CenterCrop())
                .into(binding.ivItemLogo)
        }
    }

    private fun combineNameAndArea(model: VacancyShort): String = "${model.name}, ${model.areaName}"
}
