package com.sugarspoon.github.data.sources

import com.sugarspoon.github.model.response.RepositoriesResponse
import kotlinx.coroutines.flow.flow
import retrofit2.http.*
import javax.inject.Inject

class GitHubDataSource @Inject constructor(
    serviceFactory: com.sugarspoon.data.ServiceFactory
) {

    private val service = serviceFactory.newInstance<Service>()

    fun getRepositories(page: Int) = flow {
        emit(service.getRepositories(page))
    }

    interface Service {

        @GET("search/repositories?q=language:kotlin&sort=stars")
        suspend fun getRepositories(@Query("page") page: Int): RepositoriesResponse

    }
}