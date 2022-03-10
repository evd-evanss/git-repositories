package com.sugarspoon.repositoriosgit.ui.details

import com.sugarspoon.data.model.local.RepositoryEntity
import com.sugarspoon.repositoriosgit.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor() : BaseViewModel<DetailsIntent, DetailsState>() {

    lateinit var urlRepository: String

    override fun handle(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.LoadData -> handleLoadData(intent.repositoryEntity)
            is DetailsIntent.OnClickGoToRepository -> handleGoToRepository()
        }
    }

    private fun handleLoadData(repositoryEntity: RepositoryEntity) {
        urlRepository = repositoryEntity.url
        _state.value = DetailsState.DisplayData(repositoryEntity)
    }

    private fun handleGoToRepository() {
        _state.value = DetailsState.OpenRepository(urlRepository)
    }
}