package com.sugarspoon.github.di

import com.sugarspoon.github.data.repositories.Repository
import com.sugarspoon.github.data.repositories.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface RepositoryModule {

    @Binds
    abstract fun bindsRepository(repository: RepositoryImpl): Repository

}