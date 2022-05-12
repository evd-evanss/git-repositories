package com.sugarspoon.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sugarspoon.core.utils.SingleLiveEvent

abstract class BaseViewModel<Intent, State> : ViewModel() {

    protected val _state = SingleLiveEvent<State>()
    val state: LiveData<State> = _state

    abstract fun handle(intent: Intent)
}