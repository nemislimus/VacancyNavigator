package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.viewmodels.utils.BindingFragment
import ru.practicum.android.diploma.util.emptyString

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private var searchTextWatcher: TextWatcher? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llSearchFieldContainer.ivClearIcon.setOnClickListener {
            clearQuery()
        }

        searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                showClearIcon(s.isNullOrBlank())
            }
        }

        binding.llSearchFieldContainer.etSearchQueryText.addTextChangedListener(searchTextWatcher)
    }

    override fun onDestroy() {
        binding.llSearchFieldContainer.etSearchQueryText.removeTextChangedListener(searchTextWatcher)
        searchTextWatcher = null
        super.onDestroy()
    }

    private fun showClearIcon(emptyQuery: Boolean) {
        with(binding.llSearchFieldContainer) {
            ivSearchIcon.isVisible = emptyQuery
            ivClearIcon.isVisible = !emptyQuery
        }
    }

    private fun clearQuery() {
        binding.llSearchFieldContainer.etSearchQueryText.setText(emptyString())
    }
}
