package com.kumadev.kumastream.ui.navigation

import android.net.Uri

/**
 * Type-safe(ish) destination table for the single-Activity NavHost.
 *
 * String routes (not the newer @Serializable type-safe API) are used on purpose:
 * type-safe nav needs the kotlinx-serialization plugin + dependency, and the
 * build toolchain here is deliberately minimal (AGP 9 / KSP2 — see
 * docs/NEXT-SESSION.md). Route/arg names are centralised here so screens never
 * hardcode strings.
 */
sealed class Destination(val route: String) {

    /** Home / List — the start destination (design §5.1). */
    data object Home : Destination("home")

    /**
     * Add / Edit (design §5.3). Optional [ARG_EVENT_ID]: absent → create,
     * present → edit that event. Built as P1 #8; a stub stands in for now.
     */
    data object AddEdit : Destination("add_edit?eventId={eventId}") {
        const val ARG_EVENT_ID = "eventId"

        /** null → new event, non-null → edit existing. */
        fun createRoute(eventId: String? = null): String =
            if (eventId == null) "add_edit"
            else "add_edit?$ARG_EVENT_ID=${Uri.encode(eventId)}"
    }

    /** Event detail (design §5.4). Built as P1 #9; a stub stands in for now. */
    data object Detail : Destination("event/{eventId}") {
        const val ARG_EVENT_ID = "eventId"

        fun createRoute(eventId: String): String = "event/${Uri.encode(eventId)}"
    }

    /** Category management (design §5.5). Built as P1 #12. */
    data object Categories : Destination("categories")

    /** Read-only past-events archive (design §5.7). Built as P1 #11. */
    data object Archive : Destination("archive")

    /** Settings (filters/prefs live here later). */
    data object Settings : Destination("settings")
}
