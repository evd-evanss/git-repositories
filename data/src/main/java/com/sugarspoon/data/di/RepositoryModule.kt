package com.sugarspoon.data.di

import com.sugarspoon.data.repositories.Repository
import com.sugarspoon.data.repositories.RepositoryImpl
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