package ru.practicum.android.diploma.ui.filtration.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.databinding.FragmentFiltrationPlaceOfWorkBinding
import ru.practicum.android.diploma.ui.utils.BindingFragment

class FiltrationPlaceOfWorkFragment : BindingFragment<FragmentFiltrationPlaceOfWorkBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFiltrationPlaceOfWorkBinding {
        return FragmentFiltrationPlaceOfWorkBinding.inflate(inflater, container, false)
    }
}
