package ru.practicum.android.diploma.ui.utils

import android.os.Bundle
import android.view.View
import ru.practicum.android.diploma.databinding.FragmentFiltrationBinding
import ru.practicum.android.diploma.ui.filtration.fragments.FiltrationFragmentUiDetektHelper
import ru.practicum.android.diploma.util.NumDeclension

abstract class DetektBindingFragment : BindingFragment<FragmentFiltrationBinding>(), NumDeclension {

    protected var detektHelper: FiltrationFragmentUiDetektHelper? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detektHelper = FiltrationFragmentUiDetektHelper(requireContext(), binding)
        detektHelper?.onViewCreated()
        detektHelper?.onResume()
    }
}
