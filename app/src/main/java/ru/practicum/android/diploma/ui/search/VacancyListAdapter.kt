package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyListGapBinding
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.domain.search.model.Vacancy

class VacancyListAdapter(
    private val itemClickListener: ItemClickListener,
) : ListAdapter<Vacancy, RecyclerView.ViewHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VACANCY_ITEM -> VacancyViewHolder(
                VacancyListItemBinding.inflate(layoutInflater, parent, false)
            )

            GAP_ITEM -> GapViewHolder(
                VacancyListGapBinding.inflate(layoutInflater, parent, false)
            )

            else -> throw ClassNotFoundException(R.string.unknown_viewholder_create.toString())
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VacancyViewHolder -> {
                holder.bind(currentList[position])
                holder.itemView.setOnClickListener { itemClickListener.onItemClick(currentList[position]) }
            }

            is GapViewHolder -> Unit
            else -> throw ClassNotFoundException(R.string.unknown_viewholder_bind.toString())
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (currentList[position].id == GAP_ID) GAP_ITEM else VACANCY_ITEM

    private class ItemComparator : DiffUtil.ItemCallback<Vacancy>() {
        override fun areItemsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
            return oldItem == newItem
        }
    }

    fun interface ItemClickListener {
        fun onItemClick(item: Vacancy)
    }

    companion object {
        private const val VACANCY_ITEM = 0
        private const val GAP_ITEM = 1

        private const val GAP_ID: String = "GAP"
    }
}
