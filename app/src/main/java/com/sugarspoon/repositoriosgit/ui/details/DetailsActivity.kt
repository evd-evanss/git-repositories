package com.sugarspoon.repositoriosgit.ui.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import com.sugarspoon.data.model.local.RepositoryEntity
import com.sugarspoon.repositoriosgit.R
import com.sugarspoon.repositoriosgit.base.BaseActivity
import com.sugarspoon.repositoriosgit.databinding.ActivityDetailsBinding
import com.sugarspoon.repositoriosgit.utils.loadPicture
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : BaseActivity<ActivityDetailsBinding>(
    ActivityDetailsBinding::inflate
) {

    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var repositoryEntity: RepositoryEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repositoryEntity = intent.extras?.get(DETAILS_EXTRA) as RepositoryEntity
        setupState()
        setupListener()
        viewModel.handle(DetailsIntent.LoadData(repositoryEntity))
        setupUi()
    }

    private fun setupUi() {
        setToolbar("Detalhes", true, viewGroup = binding.include2.root)
    }

    private fun setupState() = viewModel.state.observe(this) { state ->
        when (state) {
            is DetailsState.DisplayData -> displayData(state.repositoryEntity)
            is DetailsState.OpenRepository -> displayRepository(state.urlRepository)
        }
    }

    private fun setupListener() = with(binding) {
        detailGoToRepositoryTv.setOnClickListener {
            viewModel.handle(DetailsIntent.OnClickGoToRepository)
        }
    }

    private fun displayData(data: RepositoryEntity) = with(binding) {
        detailAuthorNameTv.text = getString(R.string.placeholder_author, data.author)
        detailRepositoryNametv.text = getString(R.string.placeholder_repository, data.repositoryName)
        detailForkCountTv.text = data.forks.toString()
        detailStarCountTv.text = data.stars.toString()
        detailsAvatarIv.loadPicture(data.avatar)
    }

    private fun displayRepository(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    companion object {
        private const val DETAILS_EXTRA = "DETAILS_EXTRA"

        fun Context.onDetailsIntent(event: RepositoryEntity): Intent {
            return Intent(this, DetailsActivity::class.java).putExtra(DETAILS_EXTRA, event)
        }
    }
}