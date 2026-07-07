package com.kumadev.kumastream.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kumadev.kumastream.data.local.dao.CategoryDao
import com.kumadev.kumastream.data.local.dao.EventDao
import com.kumadev.kumastream.data.local.database.KumaStreamDatabase
import com.kumadev.kumastream.data.local.seed.DefaultCategories
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        // Provider breaks the cycle: the callback needs a DAO, but the DAO is
        // obtained from the very database being built.
        categoryDaoProvider: Provider<CategoryDao>,
    ): KumaStreamDatabase =
        Room.databaseBuilder(
            context,
            KumaStreamDatabase::class.java,
            KumaStreamDatabase.NAME,
        )
            // v1 has no historical schema to migrate from; recreate on version bump
            // until real migrations are introduced.
            .fallbackToDestructiveMigration(dropAllTables = true)
            .addCallback(seedCallback(categoryDaoProvider))
            .build()

    @Provides
    fun provideEventDao(db: KumaStreamDatabase): EventDao = db.eventDao()

    @Provides
    fun provideCategoryDao(db: KumaStreamDatabase): CategoryDao = db.categoryDao()

    /**
     * Seeds the predefined categories the first time the database is created.
     * `onCreate` fires exactly once per physical DB file, so this won't re-run on
     * every launch; the repository's `seedDefaultsIfEmpty` is the belt-and-braces
     * guard for the rare case the table ends up empty afterwards.
     */
    private fun seedCallback(
        categoryDaoProvider: Provider<CategoryDao>,
    ): RoomDatabase.Callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
                categoryDaoProvider.get().upsertAll(DefaultCategories.ENTITIES)
            }
        }
    }
}
