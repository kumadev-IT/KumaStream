package com.kumadev.kumastream.domain.repository

import com.kumadev.kumastream.domain.model.EventFilters
import kotlinx.coroutines.flow.Flow

/** Reactive access to the user's persisted list filters (design §5.6). */
interface FilterPreferencesRepository {

    /** The current filters, emitting on every change. */
    val filters: Flow<EventFilters>

    suspend fun setHidePersonal(hide: Boolean)

    suspend fun setHideIndependent(hide: Boolean)

    suspend fun setHideCompleted(hide: Boolean)
}
