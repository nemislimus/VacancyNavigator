package ru.practicum.android.diploma.ui.filtration.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.RvItemRegionBinding
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.ui.filtration.adapters.viewholders.RegionViewHolder

class RegionAdapter(
    private val onClickRegion: (Area) -> Unit,
) : RecyclerView.Adapter<RegionViewHolder>() {
    val areas = ArrayList<Area>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RegionViewHolder(
            RvItemRegionBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun getItemCount(): Int = areas.size

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        val item = areas[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClickRegion(item)
        }
    }
}
