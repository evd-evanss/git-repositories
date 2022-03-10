package com.sugarspoon.data.sources

import com.sugarspoon.data.RetrofitServiceFactory
import com.sugarspoon.data.model.remote.response.RepositoriesResponse
import com.sugarspoon.data.request.SearchRequest
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.*
import javax.inject.Inject

class GitHubDataSource @Inject constructor(
    retrofit: Retrofit.Builder,
    okHttpClient: OkHttpClient
) {

    private val service =
        RetrofitServiceFactory(retrofit, okHttpClient).newInstance<Service>()

    fun getRepositories(page: Int) = flow {
        emit(service.getRepositories(page))
    }

    interface Service {

        @GET("search/repositories?q=language:kotlin&sort=stars")
        suspend fun getRepositories(@Query("page") page: Int): RepositoriesResponse

    }
}