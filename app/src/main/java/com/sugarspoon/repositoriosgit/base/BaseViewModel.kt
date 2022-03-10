package com.sugarspoon.repositoriosgit.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sugarspoon.repositoriosgit.utils.SingleLiveEvent

abstract class BaseViewModel<Intent, State> : ViewModel() {

    protected val _state = SingleLiveEvent<State>()
    val state: LiveData<State> = _state

    abstract fun handle(intent: Intent)

}