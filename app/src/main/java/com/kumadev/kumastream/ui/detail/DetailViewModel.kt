package com.kumadev.kumastream.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kumadev.kumastream.domain.repository.EventRepository
import com.kumadev.kumastream.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Backs the Detail screen (design §5.4). Streams the event by id so edits made
 * elsewhere reflect live, and exposes Complete / Delete actions. Both actions
 * set [DetailUiState.dismissed] on success; the screen then pops back.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val eventId: String =
        checkNotNull(savedStateHandle[Destination.Detail.ARG_EVENT_ID]) {
            "DetailViewModel requires a '${Destination.Detail.ARG_EVENT_ID}' argument"
        }

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            eventRepository.observeById(eventId).collect { event ->
                _uiState.update { it.copy(event = event, loading = false) }
            }
        }
    }

    fun onToggleComplete() {
        val event = _uiState.value.event ?: return
        viewModelScope.launch {
            eventRepository.setCompleted(event.id, !event.isCompleted)
            _uiState.update { it.copy(dismissed = true) }
        }
    }

    fun onDelete() {
        val event = _uiState.value.event ?: return
        viewModelScope.launch {
            eventRepository.deleteById(event.id)
            _uiState.update { it.copy(dismissed = true) }
        }
    }
}
