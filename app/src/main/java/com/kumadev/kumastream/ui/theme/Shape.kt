package com.kumadev.kumastream.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ============================================================================
// Shape scale (docs/design-language.md §4)
//   small  8dp  -> chips, inputs
//   medium 12dp -> cards, images
//   large  20dp -> sheets
// extraSmall/extraLarge are derived to keep the M3 Shapes complete.
// ============================================================================
val KumaShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),      // chips, inputs
    medium = RoundedCornerShape(12.dp),    // cards, images
    large = RoundedCornerShape(20.dp),     // sheets
    extraLarge = RoundedCornerShape(28.dp),
)
