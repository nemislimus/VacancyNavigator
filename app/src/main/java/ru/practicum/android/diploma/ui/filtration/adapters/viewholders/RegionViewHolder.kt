package ru.practicum.android.diploma.ui.filtration.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.RvItemRegionBinding
import ru.practicum.android.diploma.domain.models.Area

class RegionViewHolder(
    val binding: RvItemRegionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Area) {
        binding.tvValue.text = model.name
    }
}
