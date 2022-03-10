package com.sugarspoon.repositoriosgit.ui.details

import com.sugarspoon.data.model.local.RepositoryEntity
import com.sugarspoon.repositoriosgit.base.BaseViewModel
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