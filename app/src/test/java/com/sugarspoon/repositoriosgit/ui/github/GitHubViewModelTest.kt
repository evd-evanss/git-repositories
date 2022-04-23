package com.sugarspoon.repositoriosgit.ui.github

import androidx.lifecycle.Observer
import com.sugarspoon.data.model.local.RepositoryEntity
import com.sugarspoon.data.model.remote.response.Items
import com.sugarspoon.data.model.remote.response.RepositoriesResponse
import com.sugarspoon.data.repositories.Repository
import com.sugarspoon.data.repositories.RepositoryImpl
import com.sugarspoon.data.sources.GitHubDataSource
import com.sugarspoon.repositoriosgit.base.BaseViewModelTest
import com.sugarspoon.repositoriosgit.base.emittedOnce
import com.sugarspoon.repositoriosgit.rules.Event
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
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

    @RelaxedMockK
    private lateinit var dataSource: GitHubDataSource

    lateinit var repo: Repository

    private val fakeResponse = listOf(
        RepositoryEntity(
            repositoryName = "repositorios_git",
            forks = 2,
            stars = 5,
            avatar = "www.google.com/fake-png",
            author = "Evandro Costa,",
            url = "evd-evans/repositorio-git"
        )
    )

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repo = spyk(RepositoryImpl(dataSource))
        viewModel = GitHubViewModel(repository)
        state = spyk<Observer<GitHubState>>()
        viewModel.state.observeForever(state)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should persist screen state when app is rotated`() = runBlockingTest {
        //given
        viewModel.persistedData.addAll(fakeResponse)
        val dataPersistedExpected = viewModel.persistedData

        //when
        viewModel.handle(GitHubIntent.RefreshData)

        //then
        state emittedOnce GitHubState.UpdateData(dataPersistedExpected)
    }

    @Test
    fun `should load repositories, update pageCount, display shimmer, hide shimmer when I open the app`() = runBlocking {
        //given
        viewModel.persistedData.clear()
        val pageCount = 0
        val expected = fakeResponse
        coEvery { repository.getRepositories(pageCount) } returns flowOf(expected)

        //when
        viewModel.handle(GitHubIntent.RefreshData)

        //then
        state emittedOnce GitHubState.DisplayShimmer(isLoading = true)
        state emittedOnce GitHubState.UpdateData(expected)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = false)
    }

    @Test
    fun `should load repositories, when I swipe down the screen`() = runBlocking {
        //given
        val pageCount = 1
        val expected = fakeResponse
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
        val expected = fakeResponse
        coEvery { repository.getRepositories(counterSaved) } returns flowOf(expected)

        //when
        viewModel.handle(GitHubIntent.TryAgain(counterSaved))

        //then
        state emittedOnce GitHubState.DisplayShimmer(isLoading = true)
        state emittedOnce GitHubState.UpdateData(expected)
        state emittedOnce GitHubState.DisplayShimmer(isLoading = false)
    }

    @Test
    fun `should show error on`() = runBlockingTest {
        //given
        val counterSaved = 5
        val repositoryExpected = mockk<RepositoriesResponse>(relaxed = true)
        coEvery { dataSource.getRepositories(1) } returns flow {
            throw Throwable("Erro")
        }

        repo.getRepositories(1).test {
            expectError()
        }
        val repo = RepositoryFake()
        //coEvery { repo.getRepositories(counterSaved) } returns expected

//        repo.getRepositories(1).test {
//            //assert(expectItem() == listOf(repositoryExpected))
//            expectError()
//        }
        //when

        //then
        //state emittedOnce GitHubState.DisplayError(expected.message.orEmpty())
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

    class RepositoryFake() : Repository {

        override fun getRepositories(page: Int) = flow {
            //throw Throwable("Erro")
            emit(listOf(RepositoryEntity(
                "",
                2,
                2,
                ",",
                "",
                ""
            )))
        }
    }
}