package com.kumadev.kumastream.ui.categories

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kumadev.kumastream.domain.model.Category
import com.kumadev.kumastream.domain.model.PredefinedCategories
import com.kumadev.kumastream.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * Backs the category management screen (design §5.5). Predefined categories are
 * read-only; user categories can be created, edited and deleted. A delete of a
 * category still referenced by events is blocked by the FK (RESTRICT) and
 * surfaced as a [CategoriesUiState.message].
 */
@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.observeAll().collect { categories ->
                _uiState.update { state ->
                    state.copy(
                        loading = false,
                        rows = categories.map { category ->
                            CategoryRow(
                                category = category,
                                editable = !PredefinedCategories.isPredefined(category.id),
                            )
                        },
                    )
                }
            }
        }
    }

    /** Create (id == null) or update a user category. Blank names are ignored. */
    fun saveCategory(id: String?, name: String, color: Int, icon: String) {
        if (name.isBlank() || icon.isBlank()) return
        val category = Category(
            id = id ?: UUID.randomUUID().toString(),
            name = name.trim(),
            color = color,
            icon = icon,
        )
        viewModelScope.launch { categoryRepository.upsert(category) }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            try {
                categoryRepository.deleteById(category.id)
            } catch (_: SQLiteConstraintException) {
                _uiState.update {
                    it.copy(message = "\"${category.name}\" is used by events and can't be deleted")
                }
            }
        }
    }

    fun onMessageShown() = _uiState.update { it.copy(message = null) }
}
