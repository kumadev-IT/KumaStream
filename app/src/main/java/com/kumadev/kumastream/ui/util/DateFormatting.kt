package com.kumadev.kumastream.ui.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

// ============================================================================
// Small date/time formatting helpers for the UI layer. Locale-aware; java.time
// is native since minSdk 26. Kept UI-side so the domain stays format-free.
// ============================================================================

private val TimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

/** "09:30" — the time shown on an [com.kumadev.kumastream.domain.model.Event] card. */
fun LocalDateTime.formatTime(): String = format(TimeFormatter)

/** Uppercase weekday abbreviation, e.g. "MON" (day-header top line). */
fun LocalDate.weekdayShort(): String =
    dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).uppercase(Locale.getDefault())

/** Uppercase month abbreviation, e.g. "JUL" (day-header meta). */
fun LocalDate.monthShort(): String =
    month.getDisplayName(TextStyle.SHORT, Locale.getDefault()).uppercase(Locale.getDefault())
