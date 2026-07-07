package com.kumadev.kumastream.data.local.seed

import com.kumadev.kumastream.data.local.entity.CategoryEntity
import com.kumadev.kumastream.domain.model.PredefinedCategories

/**
 * The predefined category set inserted once when the database is first created
 * (see the Room callback in `di/DatabaseModule.kt`; also re-checked idempotently
 * by `CategoryRepository.seedDefaultsIfEmpty`).
 *
 * Categories are drawn from `memory.md` → "Predefined Categories". Colors are the
 * **harmonized v2 hues** (docs/design-language.md §2.3): a muted set in the same
 * register as Sandy Brown / Dark Cyan — mid saturation, mid-low luminance, never
 * neon — NOT the old bright palette in memory.md.
 *
 * Five hues are the locked design anchors; the remaining eight are derived in the
 * same register and are a proposal open to design review (design-language.md lists
 * the full 15-set as still-to-be-finalized):
 *   LOCKED  → Work · Health · Movies · Concerts · Video Games
 *   DERIVED → Personal Tasks · Rest & Leisure · Study · Hobbies · Cooking ·
 *             Sports · Holidays · Deadlines
 *
 * IDs are stable slugs — they are the foreign key referenced by events, so they
 * must never change once shipped.
 */
object DefaultCategories {

    val ENTITIES: List<CategoryEntity> = listOf(
        // ---- Personal Time ------------------------------------------------
        CategoryEntity(
            id = "work",
            name = "Work",
            color = 0xFFC98A54.toInt(),      // amber      — LOCKED
            icon = "🏢",
        ),
        CategoryEntity(
            id = "health",
            name = "Health & Fitness",
            color = 0xFF7E9666.toInt(),      // sage       — LOCKED
            icon = "💪",
        ),
        CategoryEntity(
            id = "personal_tasks",
            name = "Personal Tasks",
            color = 0xFF7C8794.toInt(),      // slate      — derived
            icon = "✅",
        ),
        CategoryEntity(
            id = "rest_leisure",
            name = "Rest & Leisure",
            color = 0xFF8385AB.toInt(),      // periwinkle — derived
            icon = "🧘",
        ),
        CategoryEntity(
            id = "study",
            name = "Study",
            color = 0xFF6E77A6.toInt(),      // indigo     — derived
            icon = "📚",
        ),
        CategoryEntity(
            id = "hobbies",
            name = "Hobbies",
            color = 0xFFC67F6E.toInt(),      // coral      — derived
            icon = "🎮",
        ),
        CategoryEntity(
            id = "cooking",
            name = "Cooking",
            color = 0xFFC0A45E.toInt(),      // ochre      — derived
            icon = "🍳",
        ),
        // ---- Independent --------------------------------------------------
        CategoryEntity(
            id = "movies",
            name = "Movies & Shows",
            color = 0xFF8C6F9B.toInt(),      // mauve      — LOCKED
            icon = "🎬",
        ),
        CategoryEntity(
            id = "concerts",
            name = "Concerts & Music",
            color = 0xFFB06B80.toInt(),      // dusty rose — LOCKED
            icon = "🎵",
        ),
        CategoryEntity(
            id = "sports",
            name = "Sports Events",
            color = 0xFF6FA07E.toInt(),      // emerald    — derived
            icon = "⚽",
        ),
        CategoryEntity(
            id = "video_games",
            name = "Video Games Release",
            color = 0xFF5E7C99.toInt(),      // steel      — LOCKED
            icon = "🕹️",
        ),
        CategoryEntity(
            id = "holidays",
            name = "Holidays & Events",
            color = 0xFFC06E68.toInt(),      // muted red  — derived
            icon = "🎉",
        ),
        CategoryEntity(
            id = "deadlines",
            name = "Deadlines & Releases",
            color = 0xFFA08C74.toInt(),      // taupe      — derived
            icon = "📦",
        ),
    )

    init {
        // Tripwire: the seed ids and the domain's predefined-id set must match,
        // or "is this category locked?" checks in the UI would be wrong.
        require(ENTITIES.map { it.id }.toSet() == PredefinedCategories.IDS) {
            "DefaultCategories ids drifted from PredefinedCategories.IDS"
        }
    }
}
