package com.kumadev.kumastream.ui.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kumadev.kumastream.domain.repository.EventRepository
import com.kumadev.kumastream.ui.home.DaySection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Backs the archive screen (design §5.7): past or completed events, grouped by
 * day, most-recent-first. Read-only — no mutating actions are exposed.
 */
@HiltViewModel
class ArchiveViewModel @Inject constructor(
    eventRepository: EventRepository,
) : ViewModel() {

    private val now: LocalDateTime = LocalDateTime.now()

    val uiState: StateFlow<ArchiveUiState> =
        eventRepository.observeArchived(now)
            .map { events ->
                val days = events
                    .groupBy { it.dateTime.toLocalDate() }
                    .toSortedMap(compareByDescending { it })
                    .map { (date, dayEvents) ->
                        DaySection(
                            date = date,
                            events = dayEvents.sortedByDescending { it.dateTime },
                        )
                    }
                ArchiveUiState(isLoading = false, days = days)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ArchiveUiState(isLoading = true),
            )
}
