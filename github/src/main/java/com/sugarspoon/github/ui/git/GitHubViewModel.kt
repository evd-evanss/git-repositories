package com.sugarspoon.github.ui.git

import com.sugarspoon.core.base.BaseViewModel
import com.sugarspoon.data.utils.onCollect
import com.sugarspoon.github.data.repositories.Repository
import com.sugarspoon.github.model.local.RepositoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.retry
import javax.inject.Inject

@HiltViewModel
class GitHubViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<GitHubIntent, GitHubState>() {

    private var pageCount = 0
    internal val persistedData = mutableListOf<RepositoryEntity>()

    override fun handle(intent: GitHubIntent) {
        when (intent) {
            is GitHubIntent.RefreshData -> handleInit()
            is GitHubIntent.OnScreenSwipeDown -> handlePage()
            is GitHubIntent.TryAgain -> getRepositories(intent.page)
            is GitHubIntent.OpenDetails -> handleOpenDetails(intent.repositoryEntity)
        }
    }

    private fun handleInit() {
        if (persistedData.isNotEmpty()) {
            _state.value = GitHubState.UpdateData(persistedData)
        } else {
            getRepositories(pageCount)
        }
    }

    private fun handlePage() {
        pageCount += NEXT_PAGE
        _state.postValue(GitHubState.UpdateCounter(page = pageCount))
        getRepositories(pageCount)
    }

    private fun getRepositories(page: Int) {
        repository.getRepositories(page)
            .retry(ATTEMPTS)
            .onCollect(
            onLoading = {
                _state.value = GitHubState.DisplayShimmer(isLoading = it)
            },
            onSuccess = {
                persistedData.clear()
                persistedData.addAll(it)
                _state.value = GitHubState.UpdateData(persistedData)
            },
            onError = { error ->
                _state.value = GitHubState.DisplayError(error.message.orEmpty())
            }
        )
    }

    private fun handleOpenDetails(repositoryEntity: RepositoryEntity) {
        _state.value = GitHubState.OpenDetails(repositoryEntity)
    }

    companion object {
        private const val ATTEMPTS = 1L
        private const val NEXT_PAGE = 1
    }
}