package com.kumadev.kumastream.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kumadev.kumastream.R

// ============================================================================
// Typography — Nunito (docs/design-language.md §3)
// Rounded, friendly, on-brand (bear). Nunito is OFL and bundled under
// res/font/ (the locked "bundle in res/font/" decision). Four static weights
// are shipped: 400 / 600 / 700 / 800.
// ============================================================================

val NunitoFamily = FontFamily(
    Font(R.font.nunito_regular, FontWeight.Normal),      // 400
    Font(R.font.nunito_semibold, FontWeight.SemiBold),   // 600
    Font(R.font.nunito_bold, FontWeight.Bold),           // 700
    Font(R.font.nunito_extrabold, FontWeight.ExtraBold), // 800
)

// --- M3 type scale, roles per the locked table (§3) ------------------------
// Only the roles the app uses are customized; the rest inherit Nunito so the
// whole scale stays on-brand. Sizes/line-heights in sp for font-scaling (§7).
val KumaTypography = Typography(
    // Detail title, screen headers — 24 / 30, 800
    headlineSmall = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
    ),
    // Event title (card + detail) — 16 / 22, 700
    titleMedium = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    // Notes, description — 14 / 20, 400
    bodyMedium = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    // Buttons, chips — 13 / 16, 700
    labelLarge = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 16.sp,
    ),
    // Date / time meta — 12 / 16, 600
    labelMedium = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    // Day-header labels, captions — 11 / 14, 700
    labelSmall = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
    ),
).run {
    // Ensure every remaining role also renders in Nunito (not the default).
    copy(
        displayLarge = displayLarge.copy(fontFamily = NunitoFamily),
        displayMedium = displayMedium.copy(fontFamily = NunitoFamily),
        displaySmall = displaySmall.copy(fontFamily = NunitoFamily),
        headlineLarge = headlineLarge.copy(fontFamily = NunitoFamily),
        headlineMedium = headlineMedium.copy(fontFamily = NunitoFamily),
        titleLarge = titleLarge.copy(fontFamily = NunitoFamily),
        titleSmall = titleSmall.copy(fontFamily = NunitoFamily),
        bodyLarge = bodyLarge.copy(fontFamily = NunitoFamily),
        bodySmall = bodySmall.copy(fontFamily = NunitoFamily),
    )
}
