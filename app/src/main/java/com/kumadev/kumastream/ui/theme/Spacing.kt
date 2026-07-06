package com.kumadev.kumastream.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ============================================================================
// Spacing, sizing & elevation tokens (docs/design-language.md §4)
// Base-4 scale. Exposed via LocalSpacing so composables read it from the theme:
//     val spacing = LocalSpacing.current
//     Modifier.padding(spacing.screen)
// ============================================================================

/** Base-4 spacing scale: 4, 8, 12, 16, 20, 24, 32. */
data class Spacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 20.dp,
    val xxl: Dp = 24.dp,
    val xxxl: Dp = 32.dp,

    // Semantic aliases used across screens.
    val screen: Dp = 16.dp,   // screen edge padding
    val cardGap: Dp = 8.dp,   // gap between list cards
)

/** Fixed component sizes (§4). Not scaled by font settings. */
object Dimens {
    val categoryRail: Dp = 5.dp
    val cardImageMinWidth: Dp = 60.dp
    val cardImageMinHeight: Dp = 74.dp
    val cardImageMaxWidth: Dp = 100.dp
    val cardImageMaxHeight: Dp = 124.dp
    val fab: Dp = 56.dp
    val minTouchTarget: Dp = 48.dp
    val icon: Dp = 24.dp

    // Elevation (§4): elevation is expressed via lighter surfaces + these tonal levels.
    val cardElevation: Dp = 2.dp
    val cardElevationPressed: Dp = 6.dp
    val fabElevation: Dp = 6.dp
    val fabElevationPressed: Dp = 12.dp
}

/** Provides [Spacing] to the composition; defaulted so previews work standalone. */
val LocalSpacing = staticCompositionLocalOf { Spacing() }
