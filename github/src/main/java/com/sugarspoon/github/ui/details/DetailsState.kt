package com.sugarspoon.github.ui.details

import com.sugarspoon.github.model.local.RepositoryEntity

sealed class DetailsState {
    data class DisplayData(val repositoryEntity: RepositoryEntity) : DetailsState()
    data class OpenRepository(val urlRepository: String) : DetailsState()
}