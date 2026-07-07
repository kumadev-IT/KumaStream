package com.kumadev.kumastream.di

import com.kumadev.kumastream.data.preferences.FilterPreferencesRepositoryImpl
import com.kumadev.kumastream.data.repository.CategoryRepositoryImpl
import com.kumadev.kumastream.data.repository.EventRepositoryImpl
import com.kumadev.kumastream.data.repository.ImageRepositoryImpl
import com.kumadev.kumastream.domain.repository.CategoryRepository
import com.kumadev.kumastream.domain.repository.EventRepository
import com.kumadev.kumastream.domain.repository.FilterPreferencesRepository
import com.kumadev.kumastream.domain.repository.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Binds repository interfaces to their Room-backed implementations. */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindFilterPreferencesRepository(
        impl: FilterPreferencesRepositoryImpl,
    ): FilterPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindImageRepository(impl: ImageRepositoryImpl): ImageRepository
}
