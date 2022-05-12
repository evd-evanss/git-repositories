package com.sugarspoon.github.ui.details

import com.sugarspoon.github.model.local.RepositoryEntity

sealed class DetailsIntent {
    data class OnClickGoToRepository(val url: String) : DetailsIntent()
    data class LoadData(val repositoryEntity: RepositoryEntity) : DetailsIntent()
}