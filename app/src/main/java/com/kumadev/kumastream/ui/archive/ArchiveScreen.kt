package com.kumadev.kumastream.ui.archive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kumadev.kumastream.domain.model.Event
import com.kumadev.kumastream.ui.home.DaySection
import com.kumadev.kumastream.ui.home.components.DayHeader
import com.kumadev.kumastream.ui.home.components.EventCard
import com.kumadev.kumastream.ui.theme.LocalSpacing

/**
 * Read-only past-events archive (design §5.7), reached from the Home overflow
 * menu. Same day-grouped list as Home but cards aren't tappable and there's no
 * FAB — nothing here is editable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ArchiveViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Past events") },
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
    ) { innerPadding ->
        when {
            uiState.isLoading -> Unit
            uiState.isEmpty -> EmptyArchive(Modifier.padding(innerPadding))
            else -> ArchiveList(days = uiState.days, contentPadding = innerPadding)
        }
    }
}

@Composable
private fun ArchiveList(
    days: List<DaySection>,
    contentPadding: PaddingValues,
) {
    val spacing = LocalSpacing.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = spacing.screen,
            end = spacing.screen,
            top = contentPadding.calculateTopPadding(),
            bottom = contentPadding.calculateBottomPadding() + spacing.xxxl,
        ),
        verticalArrangement = Arrangement.spacedBy(spacing.cardGap),
    ) {
        days.forEach { day ->
            item(key = "header-${day.date}") {
                DayHeader(
                    date = day.date,
                    isToday = false,
                    accent = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            items(items = day.events, key = Event::id) { event ->
                // No onClick: archive is read-only.
                EventCard(event = event)
            }
        }
    }
}

@Composable
private fun EmptyArchive(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "No past events yet",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
