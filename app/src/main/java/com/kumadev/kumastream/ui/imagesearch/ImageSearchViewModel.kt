package com.kumadev.kumastream.ui.imagesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kumadev.kumastream.domain.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Backs the image-search modal (design §5.8). One in-flight search at a time;
 * a new search cancels the previous. Errors surface as [ImageSearchUiState.error]
 * so the sheet can offer retry or let the user cancel and save without an image.
 */
@HiltViewModel
class ImageSearchViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImageSearchUiState())
    val uiState: StateFlow<ImageSearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) = _uiState.update { it.copy(query = query) }

    /** Seed the query from the event title and run the first search, once. */
    fun startWith(query: String) {
        val state = _uiState.value
        if (state.results.isEmpty() && !state.loading && state.query.isBlank()) {
            _uiState.update { it.copy(query = query) }
            search()
        }
    }

    fun search() {
        val query = _uiState.value.query
        if (query.isBlank()) return
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = false) }
            try {
                val results = imageRepository.searchImages(query)
                _uiState.update { it.copy(loading = false, results = results) }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Exception) {
                _uiState.update { it.copy(loading = false, error = true, results = emptyList()) }
            }
        }
    }

    fun onSelect(fullUrl: String) = _uiState.update {
        it.copy(selectedUrl = if (it.selectedUrl == fullUrl) null else fullUrl)
    }
}
