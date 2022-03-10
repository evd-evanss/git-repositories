package com.sugarspoon.repositoriosgit.ui.details

import com.sugarspoon.data.model.local.RepositoryEntity

sealed class DetailsState {
    data class DisplayData(val repositoryEntity: RepositoryEntity) : DetailsState()
    data class OpenRepository(val urlRepository: String) : DetailsState()
}