package com.kumadev.kumastream.ui.archive

import com.kumadev.kumastream.ui.home.DaySection

/**
 * UI state for the read-only past-events archive (design §5.7). Reuses the
 * Home [DaySection] grouping; days run most-recent-first.
 */
data class ArchiveUiState(
    val isLoading: Boolean = true,
    val days: List<DaySection> = emptyList(),
) {
    val isEmpty: Boolean get() = !isLoading && days.isEmpty()
}
