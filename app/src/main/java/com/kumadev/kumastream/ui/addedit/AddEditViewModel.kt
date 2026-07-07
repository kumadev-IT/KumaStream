package com.kumadev.kumastream.ui.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kumadev.kumastream.domain.model.Event
import com.kumadev.kumastream.domain.model.EventType
import com.kumadev.kumastream.domain.repository.CategoryRepository
import com.kumadev.kumastream.domain.repository.EventRepository
import com.kumadev.kumastream.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

/**
 * Backs the Add / Edit screen (design §5.3). Loads categories (for the dropdown)
 * and, when an [Destination.AddEdit.ARG_EVENT_ID] is present, the event being
 * edited. Title is the only required field; the category defaults to the first
 * available so [Event.color] can be cached at save time.
 */
@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val eventId: String? = savedStateHandle[Destination.AddEdit.ARG_EVENT_ID]

    /** Preserved across an edit so a re-save doesn't reset completion state. */
    private var isCompleted: Boolean = false

    private val _uiState = MutableStateFlow(AddEditUiState(isEditMode = eventId != null))
    val uiState: StateFlow<AddEditUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val existing = eventId?.let { eventRepository.getById(it) }
            if (existing != null) {
                isCompleted = existing.isCompleted
                _uiState.update {
                    it.copy(
                        title = existing.title,
                        description = existing.description.orEmpty(),
                        notes = existing.notes.orEmpty(),
                        dateTime = existing.dateTime,
                        selectedCategoryId = existing.category.id,
                        eventType = existing.eventType,
                        imageUrl = existing.imageUrl,
                    )
                }
            }
            categoryRepository.seedDefaultsIfEmpty()
        }

        viewModelScope.launch {
            categoryRepository.observeAll().collect { categories ->
                _uiState.update { state ->
                    state.copy(
                        categories = categories,
                        // Default to the first category if none chosen yet.
                        selectedCategoryId = state.selectedCategoryId
                            ?: categories.firstOrNull()?.id,
                        loading = false,
                    )
                }
            }
        }
    }

    fun onTitleChange(value: String) =
        _uiState.update { it.copy(title = value, titleError = if (value.isNotBlank()) false else it.titleError) }

    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value) }

    fun onNotesChange(value: String) = _uiState.update { it.copy(notes = value) }

    fun onDateTimeChange(value: LocalDateTime) = _uiState.update { it.copy(dateTime = value) }

    fun onCategorySelected(categoryId: String) =
        _uiState.update { it.copy(selectedCategoryId = categoryId) }

    fun onTypeChange(type: EventType) = _uiState.update { it.copy(eventType = type) }

    fun onClearImage() = _uiState.update { it.copy(imageUrl = null) }

    fun onSave() {
        val state = _uiState.value
        val category = state.selectedCategory
        if (state.title.isBlank() || category == null) {
            _uiState.update { it.copy(titleError = state.title.isBlank()) }
            return
        }
        val event = Event(
            id = eventId ?: UUID.randomUUID().toString(),
            title = state.title.trim(),
            description = state.description.trim().ifBlank { null },
            notes = state.notes.trim().ifBlank { null },
            dateTime = state.dateTime,
            category = category,
            eventType = state.eventType,
            imageUrl = state.imageUrl,
            color = category.color,
            isCompleted = isCompleted,
        )
        viewModelScope.launch {
            eventRepository.upsert(event)
            _uiState.update { it.copy(saved = true) }
        }
    }
}
