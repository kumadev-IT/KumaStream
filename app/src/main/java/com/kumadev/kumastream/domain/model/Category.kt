package com.kumadev.kumastream.domain.model

/**
 * A category as the UI layer consumes it (docs/memory.md → Domain Models).
 *
 * [color] is an ARGB Int (a harmonized muted hue, docs/design-language.md §2.3).
 * [icon] is an emoji string. The persistence-only `description` field lives on
 * the entity, not here — the UI never needs it.
 */
data class Category(
    val id: String,
    val name: String,
    val color: Int,
    val icon: String,
)
