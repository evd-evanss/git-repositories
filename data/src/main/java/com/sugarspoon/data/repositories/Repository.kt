package com.sugarspoon.data.repositories

import com.sugarspoon.data.model.local.RepositoryEntity
import com.sugarspoon.data.model.remote.response.toRepositoryEntity
import com.sugarspoon.data.sources.GitHubDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface Repository {
    fun getRepositories(page: Int): Flow<List<RepositoryEntity>>
}

class RepositoryImpl @Inject constructor(
    private val dataSource: GitHubDataSource
): Repository {

    override fun getRepositories(page: Int) = dataSource.getRepositories(page).map { response ->
        val entityList = mutableListOf<RepositoryEntity>()
        response.items.forEach {
            entityList.add(it.toRepositoryEntity())
        }
        entityList.toList()
    }
}