package ru.practicum.android.diploma.ui.search.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyListGapBinding
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.domain.models.VacancyShort

class VacancyListAdapter(
    private val itemClickListener: ItemClickListener
) : ListAdapter<VacancyShort, RecyclerView.ViewHolder>(ItemComparator()) {
    private var gapONListTop: Int = 0

    fun setGapONListTop() {
        gapONListTop = 1
    }

    override fun getItemCount(): Int {
        return currentList.size + gapONListTop
    }

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
                val item = currentList[position - gapONListTop]
                holder.bind(item)
                holder.itemView.setOnClickListener { itemClickListener.onItemClick(item) }
            }

            is GapViewHolder -> Unit
            else -> throw ClassNotFoundException(R.string.unknown_viewholder_bind.toString())
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (gapONListTop > 0 && position == 0) GAP_ITEM else VACANCY_ITEM

    fun getGap(): VacancyShort {
        return VacancyShort(
            id = GAP_ID,
            name = GAP_ID,
            employer = GAP_ID,
            areaName = GAP_ID,
            null,
            null,
            null
        )
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

    companion object {
        private const val VACANCY_ITEM = 0
        private const val GAP_ITEM = 1

        private const val GAP_ID: String = "GAP"
    }
}
