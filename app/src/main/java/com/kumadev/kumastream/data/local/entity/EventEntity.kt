package com.kumadev.kumastream.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kumadev.kumastream.domain.model.EventType

/**
 * Room entity for an event.
 *
 * [categoryId] references [CategoryEntity.id]. Deletion is RESTRICTed so a
 * category that still has events can't be removed out from under them (the UI
 * blocks deleting predefined categories anyway; this guards custom ones).
 *
 * [color] caches the category color at write time so list rendering never has
 * to join. [dateTime], [createdAt] and [updatedAt] are epoch millis.
 */
@Entity(
    tableName = "events",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("categoryId"),
        Index("dateTime"),
        Index("isCompleted"),
    ],
)
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val notes: String?,
    val dateTime: Long,
    val categoryId: String,
    val eventType: EventType,
    val imageUrl: String?,
    val color: Int,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
