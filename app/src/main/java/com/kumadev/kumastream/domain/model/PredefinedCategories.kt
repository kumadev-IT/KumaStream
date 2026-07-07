package com.kumadev.kumastream.domain.model

/**
 * The stable slug ids of the categories shipped with the app. These are seeded
 * on first launch (see `data/local/seed/DefaultCategories`) and are **locked**:
 * the UI must not let the user edit or delete them (design §5.5). User-created
 * categories use generated (UUID) ids, so membership here is the predefined test.
 *
 * This is the single source of truth for "which ids are predefined"; the seed
 * data references it with a guard so the two can never drift.
 */
object PredefinedCategories {

    val IDS: Set<String> = setOf(
        "work",
        "health",
        "personal_tasks",
        "rest_leisure",
        "study",
        "hobbies",
        "cooking",
        "movies",
        "concerts",
        "sports",
        "video_games",
        "holidays",
        "deadlines",
    )

    fun isPredefined(id: String): Boolean = id in IDS
}
