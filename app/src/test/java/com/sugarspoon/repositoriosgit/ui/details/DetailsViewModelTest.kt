package com.sugarspoon.repositoriosgit.ui.details

import androidx.lifecycle.Observer
import com.sugarspoon.data.model.local.RepositoryEntity
import com.sugarspoon.repositoriosgit.base.BaseViewModelTest
import com.sugarspoon.repositoriosgit.base.emittedOnce
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner.Silent::class)
class DetailsViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: DetailsViewModel

    @MockK
    private lateinit var state: Observer<DetailsState>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DetailsViewModel()
        state = spyk<Observer<DetailsState>>()
        viewModel.state.observeForever(state)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should show repository details when opening details screen`() = runBlockingTest {
        val expectedData = mockk<RepositoryEntity>()

        viewModel.handle(DetailsIntent.LoadData(expectedData))

        state emittedOnce DetailsState.DisplayData(expectedData)
    }

    @Test
    fun `should redirect user to repository in browser`() = runBlockingTest {
        val urlExpected = "www.google.com"

        viewModel.handle(DetailsIntent.OnClickGoToRepository(urlExpected))

        state emittedOnce DetailsState.OpenRepository(urlExpected)
    }

}