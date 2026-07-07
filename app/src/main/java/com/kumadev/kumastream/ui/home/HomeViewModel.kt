package com.kumadev.kumastream.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kumadev.kumastream.domain.repository.CategoryRepository
import com.kumadev.kumastream.domain.repository.EventRepository
import com.kumadev.kumastream.domain.repository.FilterPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Backs the Home / List screen. Streams upcoming, not-yet-completed events
 * (design §5.1) crossed with the persisted filters (design §5.6), grouped by
 * calendar day for the day-grouped LazyColumn.
 *
 * The "now" cutoff is captured once at construction — good enough for a feed
 * that already reacts to inserts/edits; a midnight-rollover refresh can come
 * later if needed.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    eventRepository: EventRepository,
    categoryRepository: CategoryRepository,
    private val filterPreferencesRepository: FilterPreferencesRepository,
) : ViewModel() {

    private val now: LocalDateTime = LocalDateTime.now()

    init {
        // Defensive idempotent guard; primary seeding runs in the Room onCreate
        // callback (di/DatabaseModule.kt).
        viewModelScope.launch { categoryRepository.seedDefaultsIfEmpty() }
    }

    val uiState: StateFlow<HomeUiState> =
        combine(
            eventRepository.observeUpcoming(now),
            filterPreferencesRepository.filters,
        ) { events, filters ->
            val days = events
                .filter { filters.matches(it) }
                .groupBy { it.dateTime.toLocalDate() }
                .toSortedMap()
                .map { (date, dayEvents) ->
                    DaySection(
                        date = date,
                        events = dayEvents.sortedBy { it.dateTime },
                    )
                }
            HomeUiState(isLoading = false, days = days, filters = filters)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState(isLoading = true),
        )

    fun onHidePersonalChange(hide: Boolean) {
        viewModelScope.launch { filterPreferencesRepository.setHidePersonal(hide) }
    }

    fun onHideIndependentChange(hide: Boolean) {
        viewModelScope.launch { filterPreferencesRepository.setHideIndependent(hide) }
    }

    fun onHideCompletedChange(hide: Boolean) {
        viewModelScope.launch { filterPreferencesRepository.setHideCompleted(hide) }
    }
}
