package me.cniekirk.jellydroid.feature.home.mobile.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import me.cniekirk.jellydroid.core.domain.model.ResumeItem

class ResumeItemPreviewProvider : PreviewParameterProvider<ResumeItem> {

    override val values = sequenceOf(
        ResumeItem(
            id = "0",
            name = "Lord of the Rings: Return of the King",
            imageUrl = "",
            aspectRatio = (3 / 2f).toDouble(),
            playedPercentage = 0.4f
        ),
        ResumeItem(
            id = "1",
            name = "Paul",
            imageUrl = "",
            aspectRatio = (3 / 2f).toDouble(),
            playedPercentage = 0.4f
        ),
        ResumeItem(
            id = "2",
            name = "What we do in the Shadows",
            imageUrl = "",
            aspectRatio = (3 / 2f).toDouble(),
            playedPercentage = 0.4f
        )
    )
}
