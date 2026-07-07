package com.kumadev.kumastream.domain.model

/**
 * The user's list filter choices (design §5.6). Toggles *hide* classes of events
 * rather than isolating one; persisted via DataStore so they survive restarts.
 */
data class EventFilters(
    val hidePersonal: Boolean = false,
    val hideIndependent: Boolean = false,
    val hideCompleted: Boolean = false,
) {
    /** True if [event] should remain visible under the current filters. */
    fun matches(event: Event): Boolean = when {
        hidePersonal && event.eventType == EventType.PERSONAL_TIME -> false
        hideIndependent && event.eventType == EventType.INDEPENDENT -> false
        hideCompleted && event.isCompleted -> false
        else -> true
    }

    val anyActive: Boolean get() = hidePersonal || hideIndependent || hideCompleted
}
