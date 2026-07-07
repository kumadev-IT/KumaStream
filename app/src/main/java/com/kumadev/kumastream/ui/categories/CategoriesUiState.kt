package com.kumadev.kumastream.ui.categories

import com.kumadev.kumastream.domain.model.Category

/**
 * A category row for the management screen (design §5.5). Predefined categories
 * are shown but [editable] is false — they can't be edited or deleted.
 */
data class CategoryRow(
    val category: Category,
    val editable: Boolean,
)

/**
 * UI state for the category management screen. [message] carries a one-shot
 * user message (e.g. a blocked delete) for a snackbar.
 */
data class CategoriesUiState(
    val loading: Boolean = true,
    val rows: List<CategoryRow> = emptyList(),
    val message: String? = null,
)
