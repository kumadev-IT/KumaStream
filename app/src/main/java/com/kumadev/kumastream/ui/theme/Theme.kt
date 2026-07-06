package com.kumadev.kumastream.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// KumaStream is dark-first in v1. A light scheme can be added post-MVP.
private val KumaDarkColorScheme = darkColorScheme(
    primary = AccentPrimary,
    secondary = AccentSecondary,
    background = BackgroundDeep,
    surface = SurfaceDark,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = ErrorRed,
)

@Composable
fun KumaStreamTheme(
    // v1 is always dark; parameter kept for future light-theme support.
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = KumaDarkColorScheme,
        content = content,
    )
}
