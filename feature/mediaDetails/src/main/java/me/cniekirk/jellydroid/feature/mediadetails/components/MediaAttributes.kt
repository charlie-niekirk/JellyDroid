package me.cniekirk.jellydroid.feature.mediadetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.cniekirk.core.jellydroid.domain.model.AgeRating
import me.cniekirk.core.jellydroid.domain.model.CommunityRating
import me.cniekirk.core.jellydroid.domain.model.MediaAttributes
import me.cniekirk.jellydroid.core.designsystem.theme.preview.CoilPreview

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun Attributes(
    mediaAttributes: MediaAttributes,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val rating = mediaAttributes.communityRating

        if (rating is CommunityRating.StarRating) {
            Attribute(
                modifier = Modifier.fillMaxRowHeight(1f),
                text = "${rating.value}",
                icon = {
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        imageVector = Icons.Default.Star,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentDescription = null
                    )
                }
            )
        }

        mediaAttributes.ageRating?.let { ageRating ->
            Attribute(
                modifier = Modifier.fillMaxRowHeight(1f),
                text = ageRating.ratingName
            )
        }

        mediaAttributes.runtime?.let { runtime ->
            Attribute(
                modifier = Modifier.fillMaxRowHeight(1f),
                text = runtime
            )
        }
    }
}

@Composable
private fun Attribute(
    text: String,
    modifier: Modifier = Modifier,
    icon: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()

        Text(
            text = text,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

const val PREVIEW_STAR_RATING = 7.6f

@Preview
@Composable
private fun MediaAttributesPreview() {
    val mediaAttributes = MediaAttributes(
        communityRating = CommunityRating.StarRating(PREVIEW_STAR_RATING),
        ageRating = AgeRating(
            ratingName = "12A",
            ratingImageUrl = null,
        ),
        runtime = "1h32m"
    )
    CoilPreview {
        Attributes(
            modifier = Modifier.padding(8.dp),
            mediaAttributes = mediaAttributes
        )
    }
}
