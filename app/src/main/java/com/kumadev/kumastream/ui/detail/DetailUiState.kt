package com.kumadev.kumastream.ui.detail

import com.kumadev.kumastream.domain.model.Event

/**
 * UI state for the Detail screen (design §5.4). [event] is null while loading or
 * if it was deleted from under us. [dismissed] flips once a Complete or Delete
 * action succeeds so the screen pops back to the list.
 */
data class DetailUiState(
    val loading: Boolean = true,
    val event: Event? = null,
    val dismissed: Boolean = false,
)
