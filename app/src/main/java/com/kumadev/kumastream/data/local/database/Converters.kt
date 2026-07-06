package com.kumadev.kumastream.data.local.database

import androidx.room.TypeConverter
import com.kumadev.kumastream.domain.model.EventType

/**
 * Room type converters. [EventType] is stored as its name (TEXT) so the schema
 * matches the SQL contract while the entity stays type-safe.
 */
class Converters {
    @TypeConverter
    fun fromEventType(value: EventType): String = value.name

    @TypeConverter
    fun toEventType(value: String): EventType = EventType.valueOf(value)
}
