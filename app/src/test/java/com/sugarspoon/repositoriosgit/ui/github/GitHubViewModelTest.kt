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
    fun `should persist screen state when app is rotated`() = runBlockingTest {
        //given
        val pageCount = 1
        val fakeDataPersisted = mutableListOf<RepositoryEntity>()
        fakeDataPersisted.add(
            RepositoryEntity(
                repositoryName = "fakeRepo",
                forks = 5,
                stars = 5,
                avatar = "fakeAvatar",
                author = "fakeAuthor",
                url = "fakeUrl"
            )
        )
        coEvery { repository.getRepositories(pageCount) } returns flowOf(fakeDataPersisted)

        //when
        viewModel.dataLocal.addAll(fakeDataPersisted)
        viewModel.handle(GitHubIntent.InitData)

        //then
        state emittedOnce GitHubState.UpdateData(fakeDataPersisted)
    }

    @Test
    fun `should load repositories, update pageCount, display shimmer, hide shimmer when I open the app`() = runBlockingTest {
        //given
        val pageCount = 1
        val expected = mockk<List<RepositoryEntity>>()
        coEvery { repository.getRepositories(pageCount) } returns flowOf(expected)

        //when
        viewModel.handle(GitHubIntent.LoadRepositories)

        //then
        state emittedOnce GitHubState.UpdateCounter(page = pageCount)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = true)
        state emittedOnce GitHubState.UpdateData(expected)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = false)
    }

    @Test
    fun `should load repositories, when I swipe down the screen`() = runBlockingTest {
        //given
        val pageCount = 1
        val expected = mockk<List<RepositoryEntity>>()
        coEvery { repository.getRepositories(pageCount) } returns flowOf(expected)

        //when
        viewModel.handle(GitHubIntent.OnScreenSwipeDown)

        //then
        state emittedOnce GitHubState.UpdateCounter(page = pageCount)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = true)
        state emittedOnce GitHubState.UpdateData(expected)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = false)
    }

    @Test
    fun `should load repositories when click in try again button`() = runBlockingTest {
        //given
        val counterSaved = 5
        val expected = mockk<List<RepositoryEntity>>()
        coEvery { repository.getRepositories(counterSaved) } returns flowOf(expected)

        //when
        viewModel.handle(GitHubIntent.TryAgain(counterSaved))

        //then
        state emittedOnce GitHubState.DisplayShimmer(isLoading = true)
        state emittedOnce GitHubState.UpdateData(expected)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = false)
    }

    @Test
    fun `should open details when click in item repository`() {
        //given
        val repositoryEntity = mockk<RepositoryEntity>()

        //when
        viewModel.handle(GitHubIntent.OpenDetails(repositoryEntity))

        //then
        state emittedOnce GitHubState.OpenDetails(repositoryEntity)
    }
}