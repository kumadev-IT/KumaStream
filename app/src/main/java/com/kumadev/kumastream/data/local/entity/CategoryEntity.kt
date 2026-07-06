package com.kumadev.kumastream.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for a category. Colors are the harmonized muted set
 * (docs/design-language.md §2.3); [color] is stored as an ARGB Int.
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val color: Int,        // ARGB, e.g. 0xFF7E9666.toInt()
    val icon: String,      // emoji, e.g. "🎬"
    val description: String? = null,
)
