package ru.practicum.android.diploma.ui.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class StateViewModel<T> : ViewModel() {

    private val stateLiveData = MutableLiveData<T>()
    fun stateObserver(): LiveData<T> = stateLiveData

    protected fun renderState(state: T) {
        stateLiveData.postValue(state)
    }
}
