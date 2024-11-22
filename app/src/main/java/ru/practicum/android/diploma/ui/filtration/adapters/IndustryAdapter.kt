package ru.practicum.android.diploma.ui.filtration.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.databinding.FilterIndustryElementBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.ui.filtration.adapters.viewholders.IndustryViewHolder
import ru.practicum.android.diploma.util.EMPTY_STRING

class IndustryAdapter(
    private val listener: IndustryClickListener,
) : ListAdapter<Industry, IndustryViewHolder>(IndustryComparator()) {

    private var checkedItemId: String = EMPTY_STRING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return IndustryViewHolder(
            FilterIndustryElementBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
        holder.binding.rbIndustryButton.setOnClickListener {
            manageListRadioButtons(item.id)
            listener.onIndustryClick(item)
        }
    }

    private fun manageListRadioButtons(clickedIndustryId: String) {
        var copyList: MutableList<Industry> = mutableListOf()
        val updateList: MutableList<Industry>
        copyList.addAll(currentList)
        val previousSelectedItemId: String
        if (checkedItemId != clickedIndustryId) {
            updateList = copyList.map { item ->
                val newItem = if (item.id == clickedIndustryId) item.copy(isSelected = true) else item
                newItem
            }.toMutableList()

            if (checkedItemId != EMPTY_STRING) {
                previousSelectedItemId = checkedItemId
                copyList = updateList.map { item ->
                    val newItem = if (item.id == previousSelectedItemId) item.copy(isSelected = false) else item
                    newItem
                }.toMutableList()
                checkedItemId = clickedIndustryId
                submitList(copyList)
            } else {
                checkedItemId = clickedIndustryId
                submitList(updateList)
            }
        }
    }

    private class IndustryComparator : DiffUtil.ItemCallback<Industry>() {
        override fun areItemsTheSame(oldItem: Industry, newItem: Industry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Industry, newItem: Industry): Boolean {
            return oldItem.id == newItem.id && oldItem.isSelected == newItem.isSelected
        }
    }

    fun interface IndustryClickListener {
        fun onIndustryClick(item: Industry)
    }
}
