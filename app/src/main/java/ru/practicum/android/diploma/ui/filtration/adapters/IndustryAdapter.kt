package ru.practicum.android.diploma.ui.filtration.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.FilterIndustryElementBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.ui.filtration.adapters.viewholders.IndustryViewHolder

class IndustryAdapter(
    private val listener: IndustryClickListener,
) : RecyclerView.Adapter<IndustryViewHolder>() {

    val industries = ArrayList<Industry>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IndustryViewHolder(
            FilterIndustryElementBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun getItemCount(): Int = industries.size

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val item = industries[position]
        holder.bind(item)
        holder.binding.rbIndustryButton.setOnClickListener {
            manageListRadioButtons(item.id)
            listener.onIndustryClick(item)
        }
    }

    private fun manageListRadioButtons(clickedIndustryId: String) {
        industries.forEachIndexed { index, industry ->

            if (industry.isSelected != (industry.id == clickedIndustryId)) {
                industries[index] = industry.copy(
                    isSelected = industry.id == clickedIndustryId
                )
                notifyItemChanged(index)
            }
        }
    }

    fun interface IndustryClickListener {
        fun onIndustryClick(item: Industry)
    }
}
