package com.kumadev.kumastream.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.kumadev.kumastream.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    /** All events, earliest first (home list is date-ascending). */
    @Query("SELECT * FROM events ORDER BY dateTime ASC")
    fun observeAll(): Flow<List<EventEntity>>

    /** Upcoming, not-yet-completed events at/after [now] — the Home feed. */
    @Query(
        "SELECT * FROM events WHERE isCompleted = 0 AND dateTime >= :now " +
            "ORDER BY dateTime ASC",
    )
    fun observeUpcoming(now: Long): Flow<List<EventEntity>>

    /** Completed or past events — the read-only archive (design §5.7). */
    @Query(
        "SELECT * FROM events WHERE isCompleted = 1 OR dateTime < :now " +
            "ORDER BY dateTime DESC",
    )
    fun observeArchived(now: Long): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE categoryId = :categoryId ORDER BY dateTime ASC")
    fun observeByCategory(categoryId: String): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    fun observeById(id: String): Flow<EventEntity?>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getById(id: String): EventEntity?

    @Upsert
    suspend fun upsert(event: EventEntity)

    @Upsert
    suspend fun upsertAll(events: List<EventEntity>)

    @Query("UPDATE events SET isCompleted = :completed, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setCompleted(id: String, completed: Boolean, updatedAt: Long)

    @Delete
    suspend fun delete(event: EventEntity)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteById(id: String)
}
