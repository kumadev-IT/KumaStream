package com.kumadev.kumastream.data.local.mapper

import com.kumadev.kumastream.data.local.entity.EventEntity
import com.kumadev.kumastream.domain.model.Category
import com.kumadev.kumastream.domain.model.Event

/**
 * [EventEntity] → domain [Event]. The entity stores only [EventEntity.categoryId],
 * so the resolved [category] must be supplied by the caller (the repository joins
 * it in). Epoch millis become a [java.time.LocalDateTime].
 */
fun EventEntity.toDomain(category: Category): Event =
    Event(
        id = id,
        title = title,
        description = description,
        notes = notes,
        dateTime = dateTime.toLocalDateTime(),
        category = category,
        eventType = eventType,
        imageUrl = imageUrl,
        color = color,
        isCompleted = isCompleted,
    )

/**
 * Domain [Event] → [EventEntity]. [categoryId] is taken from the event's category
 * and [color] is cached from the domain model. Bookkeeping timestamps aren't on
 * the domain model: pass them explicitly (the repository preserves `createdAt`
 * across updates); both default to now.
 */
fun Event.toEntity(
    createdAt: Long = System.currentTimeMillis(),
    updatedAt: Long = System.currentTimeMillis(),
): EventEntity =
    EventEntity(
        id = id,
        title = title,
        description = description,
        notes = notes,
        dateTime = dateTime.toEpochMillis(),
        categoryId = category.id,
        eventType = eventType,
        imageUrl = imageUrl,
        color = color,
        isCompleted = isCompleted,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
