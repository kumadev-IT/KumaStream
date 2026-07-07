package com.kumadev.kumastream.domain.repository

import com.kumadev.kumastream.domain.model.Event
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Domain-facing contract for event storage. Reads return fully-resolved [Event]s
 * (category joined in); time is expressed as [LocalDateTime], the repository
 * handles the epoch conversion.
 */
interface EventRepository {

    /** All events, earliest first. */
    fun observeAll(): Flow<List<Event>>

    /** Upcoming, not-yet-completed events at/after [now] — the Home feed. */
    fun observeUpcoming(now: LocalDateTime): Flow<List<Event>>

    /** Completed or past events — the read-only archive (design §5.7). */
    fun observeArchived(now: LocalDateTime): Flow<List<Event>>

    fun observeByCategory(categoryId: String): Flow<List<Event>>

    fun observeById(id: String): Flow<Event?>

    suspend fun getById(id: String): Event?

    /** Insert or update. Preserves the original `createdAt` on update. */
    suspend fun upsert(event: Event)

    suspend fun setCompleted(id: String, completed: Boolean)

    suspend fun delete(event: Event)

    suspend fun deleteById(id: String)
}
