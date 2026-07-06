package com.kumadev.kumastream.domain.model

/**
 * How an event relates to the user's time (docs/memory.md).
 * Set once at creation and never shown on the card — used only by the filter.
 */
enum class EventType {
    /** Occupies the user's own time (work, gym, dentist). */
    PERSONAL_TIME,

    /** A world event the user tracks (movie release, concert, game launch). */
    INDEPENDENT,
}
