package com.kumadev.kumastream.ui.home

import com.kumadev.kumastream.domain.model.Event
import java.time.LocalDate

/**
 * A day's worth of events in the Home feed. The list is grouped by calendar day
 * (design §5.1); [date] drives the day header (today = Sandy Brown, future =
 * Dark Cyan — see [com.kumadev.kumastream.ui.home.components.DayHeader]).
 */
data class DaySection(
    val date: LocalDate,
    val events: List<Event>,
)

/**
 * UI state for the Home / List screen. [days] is empty while [isLoading]; an
 * empty list once loaded means the empty state (design §5.1) is shown.
 */
data class HomeUiState(
    val isLoading: Boolean = true,
    val days: List<DaySection> = emptyList(),
) {
    val isEmpty: Boolean get() = !isLoading && days.isEmpty()
}
