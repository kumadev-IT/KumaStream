package com.kumadev.kumastream.data.local.mapper

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Epoch-millis (how Room stores time) ↔ [LocalDateTime] (what the UI uses).
 *
 * Conversion is done in the device's [ZoneId.systemDefault] zone: stored
 * instants are absolute, so an event always reads back at the wall-clock time
 * the user set it in their current zone. minSdk 26 gives us java.time natively —
 * no desugaring needed.
 */

fun Long.toLocalDateTime(zone: ZoneId = ZoneId.systemDefault()): LocalDateTime =
    Instant.ofEpochMilli(this).atZone(zone).toLocalDateTime()

fun LocalDateTime.toEpochMillis(zone: ZoneId = ZoneId.systemDefault()): Long =
    atZone(zone).toInstant().toEpochMilli()
