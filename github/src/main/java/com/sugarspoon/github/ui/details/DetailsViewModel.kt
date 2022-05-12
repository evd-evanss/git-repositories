package com.sugarspoon.github.ui.details

import com.sugarspoon.core.base.BaseViewModel
import com.sugarspoon.github.model.local.RepositoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor() : BaseViewModel<DetailsIntent, DetailsState>() {

    override fun handle(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.LoadData -> handleLoadData(intent.repositoryEntity)
            is DetailsIntent.OnClickGoToRepository -> handleGoToRepository(intent.url)
        }
    }

    private fun handleLoadData(repositoryEntity: RepositoryEntity) {
        _state.value = DetailsState.DisplayData(repositoryEntity)
    }

    private fun handleGoToRepository(url: String) {
        _state.value = DetailsState.OpenRepository(url)
    }
}