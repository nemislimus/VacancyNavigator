package ru.practicum.android.diploma.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.viewmodels.utils.BindingFragment
import ru.practicum.android.diploma.databinding.FragmentSearchBinding

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }
}
