package com.kumadev.kumastream.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

// ============================================================================
// KumaStream theme (docs/design-language.md)
// Dark-first, fixed brand palette. Dynamic color is intentionally OFF so the
// Sandy Brown / Dark Cyan identity is preserved on every device.
// ============================================================================

private val KumaDarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,

    background = Background,
    onBackground = TextPrimary,

    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,
    surfaceContainerHigh = SurfaceElevated,

    outline = Outline,

    error = Error,
    onError = OnError,
)

/**
 * App theme. Dark-only in v1 (the [darkTheme] param is reserved for a future
 * light scheme). Dynamic color is deliberately not used — see file header.
 * Provides [Spacing] alongside the M3 color/typography/shape systems.
 */
@Composable
fun KumaStreamTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = KumaDarkColorScheme,
            typography = KumaTypography,
            shapes = KumaShapes,
            content = content,
        )
    }
}
