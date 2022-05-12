package com.sugarspoon.github.ui.git

import com.sugarspoon.github.model.local.RepositoryEntity

sealed class GitHubIntent {
    data class TryAgain(val page: Int) : GitHubIntent()
    object OnScreenSwipeDown : GitHubIntent()
    object RefreshData : GitHubIntent()
    data class OpenDetails(val repositoryEntity: RepositoryEntity) : GitHubIntent()
}
