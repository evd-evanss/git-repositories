package com.sugarspoon.repositoriosgit.ui.details

import com.sugarspoon.data.model.local.RepositoryEntity

sealed class DetailsIntent {
    object OnClickGoToRepository : DetailsIntent()
    data class LoadData(val repositoryEntity: RepositoryEntity) : DetailsIntent()
}