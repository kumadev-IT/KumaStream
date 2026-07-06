package com.kumadev.kumastream.di

import android.content.Context
import androidx.room.Room
import com.kumadev.kumastream.data.local.dao.CategoryDao
import com.kumadev.kumastream.data.local.dao.EventDao
import com.kumadev.kumastream.data.local.database.KumaStreamDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): KumaStreamDatabase =
        Room.databaseBuilder(
            context,
            KumaStreamDatabase::class.java,
            KumaStreamDatabase.NAME,
        )
            // v1 has no historical schema to migrate from; recreate on version bump
            // until real migrations are introduced.
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideEventDao(db: KumaStreamDatabase): EventDao = db.eventDao()

    @Provides
    fun provideCategoryDao(db: KumaStreamDatabase): CategoryDao = db.categoryDao()
}
