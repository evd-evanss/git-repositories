package com.sugarspoon.repositoriosgit.ui.github

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sugarspoon.data.model.local.RepositoryEntity
import com.sugarspoon.repositoriosgit.base.BaseActivity
import com.sugarspoon.repositoriosgit.databinding.ActivityGithubBinding
import com.sugarspoon.repositoriosgit.ui.details.DetailsActivity.Companion.onDetailsIntent
import com.sugarspoon.repositoriosgit.views.MessageDialogFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GitHubActivity : BaseActivity<ActivityGithubBinding>(ActivityGithubBinding::inflate) {

    private val viewModel: GitHubViewModel by viewModels()

    lateinit var repositoriesAdapter: RepositoriesAdapter

    private var counterSaved = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUi()
        setupStates()
        setupListeners()
        viewModel.handle(GitHubIntent.RefreshData)
    }

    private fun setupUi() = with(binding) {
        repositoriesAdapter = RepositoriesAdapter(this@GitHubActivity)
        gitHubListRv.apply {
            val linearLayoutManagerReverse = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
            linearLayoutManagerReverse.stackFromEnd = true
            setLayoutManager(linearLayoutManagerReverse)
            setAdapter(repositoriesAdapter)
            addVeiledItems(DEFAULT_SHIMMER)
        }
    }

    private fun setupStates() = viewModel.state.observe(this) { state ->
        when (state) {
            is GitHubState.UpdateData -> updateData(state.data)
            is GitHubState.DisplayShimmer -> displayShimmer(state.isLoading)
            is GitHubState.DisplayLoading -> displayLoading(state.isLoading)
            is GitHubState.DisplayError -> displayError(state.error)
            is GitHubState.OpenDetails -> displayDetails(state.repositoryEntity)
            is GitHubState.UpdateCounter -> updateCounter(state.page)
        }
    }

    private fun setupListeners() = with(binding) {
        repositoriesAdapter.itemClicked = { repositoryEntity ->
            viewModel.handle(GitHubIntent.OpenDetails(repositoryEntity))
        }
        gitHubRefreshSr.setOnRefreshListener {
            gitHubRefreshSr.isRefreshing = false
            viewModel.handle(GitHubIntent.LoadRepositories)
        }
    }

    private fun updateData(data: List<RepositoryEntity>) {
        repositoriesAdapter.updateData(data)
    }

    private fun displayShimmer(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            gitHubListRv.veil()
        } else {
            gitHubListRv.unVeil()
        }
    }

    private fun displayLoading(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            gitHubLoadingPb.visibility = View.VISIBLE
        } else {
            gitHubLoadingPb.visibility = View.GONE
        }
    }

    private fun displayError(error: String) =
        MessageDialogFactory(this)
            .createError(error)
            .apply {
                show()
                actionListener = {
                    viewModel.handle(GitHubIntent.TryAgain(counterSaved))
                    dismiss()
                }
            }

    private fun displayDetails(repositoryEntity: RepositoryEntity) {
        startActivity(onDetailsIntent(repositoryEntity))
    }

    private fun updateCounter(count: Int) {
        counterSaved = count
    }

    companion object {
        private const val DEFAULT_SHIMMER = 10
    }
}