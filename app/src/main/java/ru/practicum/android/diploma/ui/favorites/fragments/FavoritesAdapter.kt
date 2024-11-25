package ru.practicum.android.diploma.ui.favorites.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.domain.models.VacancyShort
import ru.practicum.android.diploma.ui.search.rv.VacancyViewHolder

class FavoritesAdapter(
    private val itemClickListener: ItemClickListener,
) : ListAdapter<VacancyShort, VacancyViewHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        return VacancyViewHolder(
            VacancyListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item)
        holder.itemView.setOnClickListener { itemClickListener.onItemClick(item) }
    }

    private class ItemComparator : DiffUtil.ItemCallback<VacancyShort>() {
        override fun areItemsTheSame(oldItem: VacancyShort, newItem: VacancyShort): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VacancyShort, newItem: VacancyShort): Boolean {
            return oldItem == newItem
        }
    }

    fun interface ItemClickListener {
        fun onItemClick(item: VacancyShort)
    }

}
