package com.kumadev.kumastream.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kumadev.kumastream.domain.model.Event
import com.kumadev.kumastream.ui.home.components.DayHeader
import com.kumadev.kumastream.ui.home.components.EventCard
import com.kumadev.kumastream.ui.theme.LocalSpacing
import java.time.LocalDate

/**
 * Home / List (design §5.1): top bar with an overflow menu, a day-grouped feed
 * of upcoming events, and a FAB to add. Empty and loading states handled.
 * Filter toggles (design §5.6) are a later task (#10) and are not wired yet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddEvent: () -> Unit,
    onOpenEvent: (String) -> Unit,
    onOpenArchive: () -> Unit,
    onOpenCategories: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(text = "KumaStream") },
                actions = {
                    OverflowMenu(
                        onOpenArchive = onOpenArchive,
                        onOpenCategories = onOpenCategories,
                        onOpenSettings = onOpenSettings,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddEvent,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add event")
            }
        },
    ) { innerPadding ->
        when {
            uiState.isLoading -> LoadingState(Modifier.padding(innerPadding))
            uiState.isEmpty -> EmptyState(Modifier.padding(innerPadding))
            else -> EventFeed(
                days = uiState.days,
                contentPadding = innerPadding,
                onOpenEvent = onOpenEvent,
            )
        }
    }
}

@Composable
private fun EventFeed(
    days: List<DaySection>,
    contentPadding: PaddingValues,
    onOpenEvent: (String) -> Unit,
) {
    val spacing = LocalSpacing.current
    val today = remember { LocalDate.now() }

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
                    isToday = day.date == today,
                    modifier = Modifier.animateItem(),
                )
            }
            items(items = day.events, key = Event::id) { event ->
                EventCard(
                    event = event,
                    onClick = { onOpenEvent(event.id) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OverflowMenu(
    onOpenArchive: () -> Unit,
    onOpenCategories: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More options")
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(
            text = { Text("Past events") },
            onClick = { expanded = false; onOpenArchive() },
        )
        DropdownMenuItem(
            text = { Text("Categories") },
            onClick = { expanded = false; onOpenCategories() },
        )
        DropdownMenuItem(
            text = { Text("Settings") },
            onClick = { expanded = false; onOpenSettings() },
        )
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    val spacing = LocalSpacing.current
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.md),
            modifier = Modifier.padding(spacing.xxxl),
        ) {
            Icon(
                imageVector = Icons.Outlined.EventNote,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(64.dp),
            )
            Text(
                text = "No upcoming events",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Tap + to add your first one.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}
