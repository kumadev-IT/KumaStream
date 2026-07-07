package com.kumadev.kumastream.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Single app-wide Preferences DataStore. The delegate must live at file scope so
// exactly one instance is created per Context.
private val Context.appDataStore by preferencesDataStore(name = "kumastream_prefs")

/** Provides the app's [DataStore] of [Preferences] (filter toggles, future prefs). */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.appDataStore
}
