package com.kumadev.kumastream.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kumadev.kumastream.domain.model.Event
import com.kumadev.kumastream.ui.theme.Dimens
import com.kumadev.kumastream.ui.theme.LocalSpacing
import com.kumadev.kumastream.ui.util.formatTime

/**
 * Event row for the Home list (design §5.2). Layout:
 * `[5dp category rail][image 100×124][ title (2 lines) · time · category ]`.
 * Surface is Gunmetal with a ~10% veil of the category hue; category color also
 * paints the rail. No type badge (design §2.2). Completed events never appear
 * here — they live in the archive — so no dimmed state is rendered.
 *
 * Glance priority is image → title → category, matching design principle 2.
 */
@Composable
fun EventCard(
    event: Event,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    val categoryColor = Color(event.color)
    val veil = categoryColor.copy(alpha = 0.10f)
    // Fixed card height = thumbnail height + the content row's vertical padding.
    val cardHeight = Dimens.cardImageMaxHeight + spacing.md * 2

    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = Dimens.cardElevation,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.height(cardHeight)) {
            // Saturated category rail (spans the card height).
            Box(
                modifier = Modifier
                    .width(Dimens.categoryRail)
                    .fillMaxHeight()
                    .background(categoryColor),
            )
            // Content area carries the subtle category veil over the surface.
            Row(
                modifier = Modifier
                    .background(veil)
                    .padding(spacing.md),
                horizontalArrangement = Arrangement.spacedBy(spacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                EventThumbnail(
                    imageUrl = event.imageUrl,
                    emoji = event.category.icon,
                    tint = categoryColor,
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(spacing.xs),
                ) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.sm),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = event.dateTime.formatTime(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "·",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "${event.category.icon} ${event.category.name}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Fixed-size thumbnail. Shows the event image when present; otherwise a filled
 * placeholder in the category hue (~18%) with the category emoji (design §5.2).
 */
@Composable
private fun EventThumbnail(
    imageUrl: String?,
    emoji: String,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(12.dp)
    val box = modifier
        .size(width = Dimens.cardImageMaxWidth, height = Dimens.cardImageMaxHeight)
        .clip(shape)

    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null, // decorative; title conveys the meaning
            contentScale = ContentScale.Crop,
            modifier = box,
        )
    } else {
        Box(
            modifier = box.background(tint.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.clearAndSetSemantics {},
            )
        }
    }
}
