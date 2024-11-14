package ru.practicum.android.diploma.ui.utils

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.viewbinding.ViewBinding

abstract class MenuBindingFragment<T : ViewBinding> : BindingFragment<T>(), MenuProvider {

    abstract fun getToolbarPanel(): Toolbar
    abstract fun getMenuRes(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
        (requireActivity() as AppCompatActivity).setSupportActionBar(getToolbarPanel())
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(getMenuRes(), menu)
    }

}
