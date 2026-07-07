package com.kumadev.kumastream.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kumadev.kumastream.ui.theme.LocalSpacing
import com.kumadev.kumastream.ui.util.monthShort
import com.kumadev.kumastream.ui.util.weekdayShort
import java.time.LocalDate

/**
 * Day-group header for the Home list (design §5.1): big day number + a
 * weekday·month meta line + a hairline. The accent follows the "warm now, cool
 * later" rhythm — today is Sandy Brown (primary), any future day is Dark Cyan
 * (secondary). Month markers on the right are a separate, deferred design task.
 */
@Composable
fun DayHeader(
    date: LocalDate,
    isToday: Boolean,
    modifier: Modifier = Modifier,
    accent: Color = if (isToday) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    },
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = spacing.lg, bottom = spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.md),
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.headlineSmall,
            color = accent,
        )
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = date.weekdayShort(),
                style = MaterialTheme.typography.labelSmall,
                color = accent,
            )
            Text(
                text = date.monthShort(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(start = spacing.sm),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}
