package com.kumadev.kumastream.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kumadev.kumastream.ui.addedit.AddEditScreen
import com.kumadev.kumastream.ui.archive.ArchiveScreen
import com.kumadev.kumastream.ui.categories.CategoriesScreen
import com.kumadev.kumastream.ui.detail.DetailScreen
import com.kumadev.kumastream.ui.home.HomeScreen
import com.kumadev.kumastream.ui.theme.KumaStreamTheme

// --- Shared-axis X transitions (design §6: medium 250ms, emphasized) --------
private const val TransitionMs = 250
private const val SlideOffsetDivisor = 6 // ~ a small horizontal shift, not full-width

private fun AnimatedContentTransitionScope<*>.enterPush(): EnterTransition =
    slideInHorizontally(tween(TransitionMs)) { it / SlideOffsetDivisor } +
        fadeIn(tween(TransitionMs))

private fun AnimatedContentTransitionScope<*>.exitPush(): ExitTransition =
    slideOutHorizontally(tween(TransitionMs)) { -it / SlideOffsetDivisor } +
        fadeOut(tween(TransitionMs))

private fun AnimatedContentTransitionScope<*>.popEnterPush(): EnterTransition =
    slideInHorizontally(tween(TransitionMs)) { -it / SlideOffsetDivisor } +
        fadeIn(tween(TransitionMs))

private fun AnimatedContentTransitionScope<*>.popExitPush(): ExitTransition =
    slideOutHorizontally(tween(TransitionMs)) { it / SlideOffsetDivisor } +
        fadeOut(tween(TransitionMs))

/**
 * The single-Activity navigation graph. Home is the start destination and is
 * fully implemented (P0 #7); the other destinations are stubs that later tasks
 * (Add/Edit #8, Detail #9, Categories #12, Archive #11) replace in place — the
 * routes and navigation actions already work end-to-end.
 */
@Composable
fun KumaNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Home.route,
        modifier = modifier,
        enterTransition = { enterPush() },
        exitTransition = { exitPush() },
        popEnterTransition = { popEnterPush() },
        popExitTransition = { popExitPush() },
    ) {
        composable(Destination.Home.route) {
            HomeScreen(
                onAddEvent = { navController.navigate(Destination.AddEdit.createRoute()) },
                onOpenEvent = { id -> navController.navigate(Destination.Detail.createRoute(id)) },
                onOpenArchive = { navController.navigate(Destination.Archive.route) },
                onOpenCategories = { navController.navigate(Destination.Categories.route) },
                onOpenSettings = { navController.navigate(Destination.Settings.route) },
            )
        }

        composable(
            route = Destination.AddEdit.route,
            arguments = listOf(
                navArgument(Destination.AddEdit.ARG_EVENT_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) {
            AddEditScreen(onDone = navController::popBackStack)
        }

        composable(
            route = Destination.Detail.route,
            arguments = listOf(
                navArgument(Destination.Detail.ARG_EVENT_ID) { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(Destination.Detail.ARG_EVENT_ID)
            DetailScreen(
                onBack = navController::popBackStack,
                onEdit = {
                    if (id != null) navController.navigate(Destination.AddEdit.createRoute(id))
                },
            )
        }

        composable(Destination.Categories.route) {
            CategoriesScreen(onBack = navController::popBackStack)
        }

        composable(Destination.Archive.route) {
            ArchiveScreen(onBack = navController::popBackStack)
        }

        composable(Destination.Settings.route) {
            StubDestination(title = "Settings", onBack = navController::popBackStack)
        }
    }
}

/**
 * Temporary placeholder for a destination whose real screen is a later backlog
 * item. Keeps the nav graph complete so navigation can be exercised now.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StubDestination(
    title: String,
    onBack: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "$title — coming soon",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun KumaNavHostPreview() {
    KumaStreamTheme {
        KumaNavHost()
    }
}
