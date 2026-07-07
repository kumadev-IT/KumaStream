package com.kumadev.kumastream.ui.imagesearch

import com.kumadev.kumastream.domain.model.ImageResult

/**
 * State for the image-search modal (design §5.8). [selectedUrl] is the *full*
 * url of the tapped tile (what gets saved onto the event).
 */
data class ImageSearchUiState(
    val query: String = "",
    val loading: Boolean = false,
    val results: List<ImageResult> = emptyList(),
    val error: Boolean = false,
    val selectedUrl: String? = null,
) {
    val isEmptyResult: Boolean get() = !loading && !error && results.isEmpty()
}
