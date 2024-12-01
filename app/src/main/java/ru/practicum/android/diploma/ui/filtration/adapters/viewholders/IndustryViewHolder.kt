package ru.practicum.android.diploma.ui.filtration.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.FilterIndustryElementBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustryViewHolder(
    val binding: FilterIndustryElementBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Industry) {
        binding.tvIndustryValue.text = model.name
        binding.rbIndustryButton.isChecked = model.isSelected
    }
}
