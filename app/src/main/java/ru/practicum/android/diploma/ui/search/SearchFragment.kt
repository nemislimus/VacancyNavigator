package ru.practicum.android.diploma.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.util.emptyString
import ru.practicum.android.diploma.viewmodels.utils.MenuBindingFragment

class SearchFragment : MenuBindingFragment<FragmentSearchBinding>() {

    private var searchTextWatcher: TextWatcher? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun getToolbarPanel(): Toolbar {
        return binding.tbSearchToolBar
    }

    override fun getMenuRes(): Int {
        return R.menu.search_toolbar_menu
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        goToFilter()
        return true
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
                setSearchIcon(s.isNullOrBlank())
                showIntro(s.isNullOrBlank())
            }
        }

        binding.llSearchFieldContainer.etSearchQueryText.addTextChangedListener(searchTextWatcher)
    }

    override fun onDestroy() {
        binding.llSearchFieldContainer.etSearchQueryText.removeTextChangedListener(searchTextWatcher)
        searchTextWatcher = null
        super.onDestroy()
    }

    private fun setSearchIcon(queryIsEmpty: Boolean) {
        with(binding.llSearchFieldContainer) {
            ivSearchIcon.isVisible = queryIsEmpty
            ivClearIcon.isVisible = !queryIsEmpty
        }
    }

    private fun showIntro(queryIsEmpty: Boolean) {
        binding.ivIntroPicture.isVisible = queryIsEmpty
    }

    private fun clearQuery() {
        binding.llSearchFieldContainer.etSearchQueryText.setText(emptyString())
    }

    private fun goToFilter() {
        findNavController().navigate(
            R.id.action_searchFragment_to_filtrationFragment
        )
    }
}
