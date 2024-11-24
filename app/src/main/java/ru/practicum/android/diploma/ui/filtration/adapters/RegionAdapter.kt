package ru.practicum.android.diploma.ui.filtration.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.databinding.RvItemRegionBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.filtration.adapters.viewholders.RegionViewHolder

class RegionAdapter(
    private val onClickRegion: (Area) -> Unit,
) : ListAdapter<Area, RegionViewHolder>(RegionComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RegionViewHolder(
            RvItemRegionBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClickRegion(item)
        }
    }

    private class RegionComparator : DiffUtil.ItemCallback<Area>() {
        override fun areItemsTheSame(oldItem: Area, newItem: Area): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Area, newItem: Area): Boolean {
            return oldItem.id == newItem.id
        }
    }

}
