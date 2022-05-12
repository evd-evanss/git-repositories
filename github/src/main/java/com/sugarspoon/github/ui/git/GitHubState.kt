package com.sugarspoon.github.ui.git

import com.sugarspoon.github.model.local.RepositoryEntity

sealed class GitHubState {
    data class UpdateData(val data: List<RepositoryEntity>) : GitHubState()
    data class DisplayError(val error: String) : GitHubState()
    data class DisplayShimmer(val isLoading: Boolean) : GitHubState()
    data class DisplayLoading(val isLoading: Boolean) : GitHubState()
    data class OpenDetails(val repositoryEntity: RepositoryEntity) : GitHubState()
    data class UpdateCounter(val page: Int) : GitHubState()
}
