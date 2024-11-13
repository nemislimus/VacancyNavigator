package ru.practicum.android.diploma.ui.filtration

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentFiltrationBinding
import ru.practicum.android.diploma.ui.utils.BindingFragment

open class FiltrationFragment : BindingFragment<FragmentFiltrationBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFiltrationBinding {
        return FragmentFiltrationBinding.inflate(inflater, container, false)
    }
}
