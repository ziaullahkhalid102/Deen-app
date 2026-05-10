package com.deenapp.di

import com.deenapp.data.repository.DeenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDeenRepository(): DeenRepository {
        return DeenRepository()
    }
}
