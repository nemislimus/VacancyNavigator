package ru.practicum.android.diploma.ui.search.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyListGapBinding
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.domain.models.VacancyShort

class VacancyListAdapter(private val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var gapONListTop: Int = 0
    val vacancies = ArrayList<VacancyShort>()

    fun setGapONListTop() {
        gapONListTop = 1
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
                val item = vacancies[position - gapONListTop]
                holder.bind(item)
                holder.itemView.setOnClickListener { itemClickListener.onItemClick(item) }
            }

            is GapViewHolder -> Unit
            else -> throw ClassNotFoundException(R.string.unknown_viewholder_bind.toString())
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (gapONListTop > 0 && position == 0) GAP_ITEM else VACANCY_ITEM

    override fun getItemCount(): Int = vacancies.size + gapONListTop

    fun interface ItemClickListener {
        fun onItemClick(item: VacancyShort)
    }

    companion object {
        private const val VACANCY_ITEM = 0
        private const val GAP_ITEM = 1
    }
}
