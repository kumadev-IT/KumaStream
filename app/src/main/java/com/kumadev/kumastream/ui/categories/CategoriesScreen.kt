package com.kumadev.kumastream.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kumadev.kumastream.domain.model.Category
import com.kumadev.kumastream.ui.theme.LocalSpacing

// Harmonized muted palette (design §2.3) offered for user categories, plus the
// Dark Cyan accent — same register as the predefined hues.
private val CategoryPalette = listOf(
    Color(0xFFC98A54), Color(0xFF7E9666), Color(0xFF7C8794), Color(0xFF8385AB),
    Color(0xFF6E77A6), Color(0xFFC67F6E), Color(0xFFC0A45E), Color(0xFF8C6F9B),
    Color(0xFFB06B80), Color(0xFF6FA07E), Color(0xFF5E7C99), Color(0xFFC06E68),
    Color(0xFFA08C74), Color(0xFF5A9393),
)

private val EmojiPresets = listOf(
    "🏢", "💪", "✅", "🧘", "📚", "🎮", "🍳", "🎬", "🎵", "⚽",
    "🕹️", "🎉", "📦", "✈️", "🎂", "🩺", "💼", "🎨",
)

/**
 * Category management (design §5.5): list of categories with a color dot; user
 * categories can be edited/deleted, predefined ones are locked. FAB opens the
 * add/edit dialog (name · color · emoji).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoriesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // null = dialog closed; Editing holds add (category == null) or edit target.
    var editorTarget by remember { mutableStateOf<EditorTarget?>(null) }

    androidx.compose.runtime.LaunchedEffect(uiState.message) {
        uiState.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onMessageShown()
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Categories") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { editorTarget = EditorTarget(null) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add category")
            }
        },
    ) { innerPadding ->
        val spacing = LocalSpacing.current
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = spacing.screen,
                end = spacing.screen,
                top = innerPadding.calculateTopPadding() + spacing.sm,
                bottom = innerPadding.calculateBottomPadding() + spacing.xxxl,
            ),
            verticalArrangement = Arrangement.spacedBy(spacing.xs),
        ) {
            items(items = uiState.rows, key = { it.category.id }) { row ->
                CategoryListItem(
                    row = row,
                    onEdit = { editorTarget = EditorTarget(row.category) },
                    onDelete = { viewModel.deleteCategory(row.category) },
                )
            }
        }
    }

    editorTarget?.let { target ->
        CategoryEditorDialog(
            initial = target.category,
            onDismiss = { editorTarget = null },
            onSave = { id, name, color, icon ->
                viewModel.saveCategory(id, name, color, icon)
                editorTarget = null
            },
        )
    }
}

/** Wrapper so a null category (add) is still a non-null dialog-open signal. */
private data class EditorTarget(val category: Category?)

@Composable
private fun CategoryListItem(
    row: CategoryRow,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        Text(text = row.category.icon, style = MaterialTheme.typography.titleMedium)
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(Color(row.category.color)),
        )
        Text(
            text = row.category.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        if (row.editable) {
            IconButton(onClick = onEdit) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit ${row.category.name}",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete ${row.category.name}",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        } else {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = "Predefined category",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun CategoryEditorDialog(
    initial: Category?,
    onDismiss: () -> Unit,
    onSave: (id: String?, name: String, color: Int, icon: String) -> Unit,
) {
    val spacing = LocalSpacing.current
    var name by remember { mutableStateOf(initial?.name.orEmpty()) }
    var color by remember { mutableStateOf(initial?.color ?: CategoryPalette.first().toArgb()) }
    var icon by remember { mutableStateOf(initial?.icon ?: EmojiPresets.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "New category" else "Edit category") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(spacing.md)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text("Color", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                SwatchPicker(
                    swatches = CategoryPalette,
                    selectedArgb = color,
                    onPick = { color = it.toArgb() },
                )
                Text("Emoji", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                EmojiPicker(
                    emojis = EmojiPresets,
                    selected = icon,
                    onPick = { icon = it },
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(initial?.id, name, color, icon) },
                enabled = name.isNotBlank(),
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SwatchPicker(
    swatches: List<Color>,
    selectedArgb: Int,
    onPick: (Color) -> Unit,
) {
    val spacing = LocalSpacing.current
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        swatches.forEach { swatch ->
            val selected = swatch.toArgb() == selectedArgb
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(swatch)
                    .then(
                        if (selected) {
                            Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                        } else {
                            Modifier
                        },
                    )
                    .clickable { onPick(swatch) },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmojiPicker(
    emojis: List<String>,
    selected: String,
    onPick: (String) -> Unit,
) {
    val spacing = LocalSpacing.current
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        emojis.forEach { emoji ->
            val isSelected = emoji == selected
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                    )
                    .clickable { onPick(emoji) },
                contentAlignment = Alignment.Center,
            ) {
                Text(text = emoji, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
