package com.kumadev.kumastream.domain.model

import java.time.LocalDateTime

/**
 * An event as the UI layer consumes it (docs/memory.md → Domain Models).
 *
 * Unlike [com.kumadev.kumastream.data.local.entity.EventEntity], this carries
 * the resolved [category] object (not just an id) and a [LocalDateTime] instead
 * of epoch millis. [color] is the category color cached at write time so lists
 * render without a join. Bookkeeping fields (createdAt/updatedAt) stay on the
 * entity — the UI doesn't surface them.
 */
data class Event(
    val id: String,
    val title: String,
    val description: String?,
    val notes: String?,
    val dateTime: LocalDateTime,
    val category: Category,
    val eventType: EventType,
    val imageUrl: String?,
    val color: Int,
    val isCompleted: Boolean,
)
