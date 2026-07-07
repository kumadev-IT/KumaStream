package com.kumadev.kumastream.ui.addedit

import com.kumadev.kumastream.domain.model.Category
import com.kumadev.kumastream.domain.model.EventType
import java.time.LocalDateTime

/**
 * UI state for the Add / Edit screen (design §5.3). One flat, form-shaped state:
 * the fields are the editable form, [categories] feeds the dropdown, and the
 * transient flags drive navigation and validation.
 *
 * [isEditMode] switches the header/label between "New" and "Edit". [titleError]
 * is non-null only after a failed save attempt with a blank title (inline
 * validation under the Title field). [saved] flips to true once persisted so the
 * screen can pop back.
 */
data class AddEditUiState(
    val isEditMode: Boolean = false,
    val loading: Boolean = true,
    val title: String = "",
    val description: String = "",
    val notes: String = "",
    val dateTime: LocalDateTime = LocalDateTime.now().plusHours(1).withMinute(0).withSecond(0).withNano(0),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val eventType: EventType = EventType.PERSONAL_TIME,
    val imageUrl: String? = null,
    val titleError: Boolean = false,
    val saved: Boolean = false,
) {
    val selectedCategory: Category?
        get() = categories.firstOrNull { it.id == selectedCategoryId }

    /** Save is enabled once a category exists to attach the event to. */
    val canSave: Boolean get() = selectedCategory != null
}
