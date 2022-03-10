package com.sugarspoon.repositoriosgit.ui.github

import androidx.lifecycle.Observer
import com.sugarspoon.data.model.local.RepositoryEntity
import com.sugarspoon.data.repositories.RepositoryImpl
import com.sugarspoon.repositoriosgit.base.BaseViewModelTest
import com.sugarspoon.repositoriosgit.base.emittedOnce
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
class GitHubViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: GitHubViewModel

    @MockK
    private lateinit var state: Observer<GitHubState>

    @RelaxedMockK
    private lateinit var repository: RepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = GitHubViewModel(repository)
        state = spyk<Observer<GitHubState>>()
        viewModel.state.observeForever(state)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should init data when open app first time`() = runBlockingTest {
        val pageCount = 1
        val expected = mockk<List<RepositoryEntity>>()
        coEvery { repository.getRepositories(pageCount) } returns flowOf(expected)

        viewModel.dataLocal.clear()
        viewModel.handle(GitHubIntent.InitData)

        state emittedOnce GitHubState.UpdateCounter(page = pageCount)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = true)
        state emittedOnce GitHubState.UpdateData(expected)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = false)
    }

    @Test
    fun `should load repositories, update pageCount, display shimmer, hide shimmer when I open the app`() = runBlockingTest {
        val pageCount = 1
        val expected = mockk<List<RepositoryEntity>>()
        coEvery { repository.getRepositories(pageCount) } returns flowOf(expected)

        viewModel.handle(GitHubIntent.LoadRepositories)

        state emittedOnce GitHubState.UpdateCounter(page = pageCount)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = true)
        state emittedOnce GitHubState.UpdateData(expected)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = false)
    }

    @Test
    fun `should load repositories, when I swipe down the screen`() = runBlockingTest {
        val pageCount = 1
        val expected = mockk<List<RepositoryEntity>>()
        coEvery { repository.getRepositories(pageCount) } returns flowOf(expected)

        viewModel.handle(GitHubIntent.OnScreenSwipeDown)

        state emittedOnce GitHubState.UpdateCounter(page = pageCount)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = true)
        state emittedOnce GitHubState.UpdateData(expected)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = false)
    }

    @Test
    fun `should load repositories when click in try again button`() = runBlockingTest {
        val counterSaved = 5
        val expected = mockk<List<RepositoryEntity>>()
        coEvery { repository.getRepositories(counterSaved) } returns flowOf(expected)

        viewModel.handle(GitHubIntent.TryAgain(counterSaved))

        state emittedOnce GitHubState.DisplayShimmer(isLoading = true)
        state emittedOnce GitHubState.UpdateData(expected)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = false)
    }

    @Test
    fun `should open details when click in item repository`() {
        val repositoryEntity = mockk<RepositoryEntity>()

        viewModel.handle(GitHubIntent.OpenDetails(repositoryEntity))

        state emittedOnce GitHubState.OpenDetails(repositoryEntity)
    }
}