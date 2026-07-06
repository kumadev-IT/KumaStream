package com.kumadev.kumastream.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================================
// KumaStream color tokens — locked palette (docs/design-language.md v2)
// Dark-first, warm-neutral. Elevation via progressively lighter surfaces.
// Built on the 5-color brand palette; supporting tones are derived.
// ============================================================================

// --- Core surfaces & neutrals (§2.1) ---------------------------------------
val Background = Color(0xFF282A2E)        // Shadow Grey        -> background
val Surface = Color(0xFF343742)           // Gunmetal           -> surface
val SurfaceVariant = Color(0xFF3E414C)    // derived            -> surfaceVariant
val SurfaceElevated = Color(0xFF474A56)   // derived            -> surfaceContainerHigh
val Outline = Color(0xFF4C4F5A)           // derived            -> outline

// --- Text / on-colors ------------------------------------------------------
val TextPrimary = Color(0xFFFFFBFE)       // Snow  -> onSurface / onBackground
val TextSecondary = Color(0xFFB7B5BA)     // derived -> onSurfaceVariant
val TextDisabled = Color(0xFF7E7C82)      // derived (no direct M3 role)

// --- Brand accents (§2.1) --------------------------------------------------
val Primary = Color(0xFFF5A65B)           // Sandy Brown -> primary (FAB, TODAY)
val OnPrimary = Color(0xFF282A2E)         // dark text on sandy
val Secondary = Color(0xFF5A9393)         // Dark Cyan -> secondary (future/complete)
val OnSecondary = Color(0xFF12201F)       // dark text on cyan (>=4.5:1)

val Error = Color(0xFFC05E4E)             // derived warm red -> error
val OnError = Color(0xFF282A2E)           // dark text on warm red

// ============================================================================
// Category colors — harmonized muted set (§2.3)
// Same register as the brand accents (mid saturation, mid-low luminance),
// never neon. Applied as a 5dp saturated left rail + ~10% veil of the same hue.
// NOTE: only the sample hues are locked so far; the full 15-category set is a
// pending design task. Keep additions in this same muted register.
// ============================================================================
object CategoryColors {
    val Health = Color(0xFF7E9666)        // sage
    val Movies = Color(0xFF8C6F9B)        // mauve
    val Concerts = Color(0xFFB06B80)      // dusty rose
    val VideoGames = Color(0xFF5E7C99)    // steel
    val Work = Color(0xFFC98A54)          // amber

    /** Sample set defined so far — for previews/defaults, not the final 15. */
    val sampleSet: List<Color> = listOf(Health, Movies, Concerts, VideoGames, Work)
}
