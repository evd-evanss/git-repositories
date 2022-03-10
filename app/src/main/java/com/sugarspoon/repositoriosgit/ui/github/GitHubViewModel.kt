package com.sugarspoon.repositoriosgit.ui.github

import com.sugarspoon.data.model.local.RepositoryEntity
import com.sugarspoon.data.repositories.Repository
import com.sugarspoon.repositoriosgit.base.BaseViewModel
import com.sugarspoon.repositoriosgit.utils.onCollect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.retry
import javax.inject.Inject

@HiltViewModel
class GitHubViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<GitHubIntent, GitHubState>() {

    private var pageCount = 0
    internal var dataLocal = mutableListOf<RepositoryEntity>()

    override fun handle(intent: GitHubIntent) {
        when (intent) {
            is GitHubIntent.InitData -> handleInit()
            is GitHubIntent.LoadRepositories -> handlePage()
            is GitHubIntent.OnScreenSwipeDown -> handlePage()
            is GitHubIntent.TryAgain -> getRepositories(intent.page)
            is GitHubIntent.OpenDetails -> handleOpenDetails(intent.repositoryEntity)
        }
    }

    private fun handleInit() {
        if (dataLocal.isEmpty()) {
            handlePage()
        } else {
            _state.value = GitHubState.UpdateData(dataLocal)
        }
    }

    private fun handlePage() {
        pageCount += 1
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
                dataLocal.clear()
                dataLocal.addAll(it)
                _state.value = GitHubState.UpdateData(it)
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
    }
}