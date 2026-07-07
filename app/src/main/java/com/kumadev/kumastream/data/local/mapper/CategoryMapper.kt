package com.kumadev.kumastream.data.local.mapper

import com.kumadev.kumastream.data.local.entity.CategoryEntity
import com.kumadev.kumastream.domain.model.Category

/** [CategoryEntity] → domain [Category]. Drops the persistence-only description. */
fun CategoryEntity.toDomain(): Category =
    Category(
        id = id,
        name = name,
        color = color,
        icon = icon,
    )

/**
 * Domain [Category] → [CategoryEntity]. The domain model carries no description,
 * so pass one explicitly to preserve it on an update (defaults to null).
 */
fun Category.toEntity(description: String? = null): CategoryEntity =
    CategoryEntity(
        id = id,
        name = name,
        color = color,
        icon = icon,
        description = description,
    )
