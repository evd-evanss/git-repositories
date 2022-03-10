package com.sugarspoon.repositoriosgit.ui.details

import com.sugarspoon.data.model.local.RepositoryEntity

sealed class DetailsIntent {
    data class OnClickGoToRepository(val url: String) : DetailsIntent()
    data class LoadData(val repositoryEntity: RepositoryEntity) : DetailsIntent()
}