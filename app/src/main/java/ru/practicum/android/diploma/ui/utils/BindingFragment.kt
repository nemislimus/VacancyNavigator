package ru.practicum.android.diploma.ui.utils

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BindingFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null
    protected val binding: T get() = _binding!!
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // no code
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // no code
        }

        override fun afterTextChanged(s: Editable?) {
            onTextInput(s.toString())
        }
    }

    abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = createBinding(inflater, container)
        return binding.root
    }

    open fun onTextInput(text: String) {}

    open fun onDestroyFragment() {}

    final override fun onDestroyView() {
        super.onDestroyView()
        onDestroyFragment()
        _binding = null
    }

    final override fun onDestroy() {
        super.onDestroy()
    }

    final override fun onDetach() {
        super.onDetach()
    }
}
