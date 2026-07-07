package com.kumadev.kumastream.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.kumadev.kumastream.domain.model.EventFilters
import com.kumadev.kumastream.domain.repository.FilterPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DataStore-backed [FilterPreferencesRepository]. Reads recover from a corrupt
 * store by falling back to defaults rather than crashing the list.
 */
@Singleton
class FilterPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : FilterPreferencesRepository {

    private object Keys {
        val HidePersonal = booleanPreferencesKey("hide_personal")
        val HideIndependent = booleanPreferencesKey("hide_independent")
        val HideCompleted = booleanPreferencesKey("hide_completed")
    }

    override val filters: Flow<EventFilters> = dataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { prefs ->
            EventFilters(
                hidePersonal = prefs[Keys.HidePersonal] ?: false,
                hideIndependent = prefs[Keys.HideIndependent] ?: false,
                hideCompleted = prefs[Keys.HideCompleted] ?: false,
            )
        }

    override suspend fun setHidePersonal(hide: Boolean) {
        dataStore.edit { it[Keys.HidePersonal] = hide }
    }

    override suspend fun setHideIndependent(hide: Boolean) {
        dataStore.edit { it[Keys.HideIndependent] = hide }
    }

    override suspend fun setHideCompleted(hide: Boolean) {
        dataStore.edit { it[Keys.HideCompleted] = hide }
    }
}
