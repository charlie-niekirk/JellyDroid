package me.cniekirk.core.jellydroid.domain.mapping

import me.cniekirk.core.jellydroid.domain.model.AgeRating
import me.cniekirk.core.jellydroid.domain.model.CommunityRating
import me.cniekirk.core.jellydroid.domain.model.MediaAttributes
import me.cniekirk.core.jellydroid.domain.model.MediaDetailsUiModel
import org.jellyfin.sdk.model.api.BaseItemDto
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MediaDetailsMapper @Inject constructor() {

    fun toUiModel(dataModel: BaseItemDto, baseUrl: String): MediaDetailsUiModel {
        val rating = dataModel.communityRating?.let {
            CommunityRating.StarRating(it)
        } ?: CommunityRating.NoRating

        return MediaDetailsUiModel(
            synopsis = dataModel.overview,
            primaryImageUrl = "$baseUrl/Items/${dataModel.id}/Images/Backdrop",
            mediaAttributes = MediaAttributes(
                communityRating = rating,
                ageRating = dataModel.officialRating?.let {
                    AgeRating(
                        ratingName = it,
                        ratingImageUrl = ""
                    )
                },
                runtime = dataModel.runTimeTicks?.toRuntime()
            )
        )
    }

    private fun Long.toRuntime(): String {
        val duration = this.toDuration(DurationUnit.MICROSECONDS)
        val components = duration.toComponents { hours, minutes, seconds, _ ->
            Triple(hours, minutes, seconds)
        }

        return buildString {
            components.run {
                val correctH = if (second > 0) "h " else "h"
                val correctM = if (first == 0L && third > 0) "m " else "m"

                if (first > 0) append(first).append(correctH)
                if (second > 0) append(second).append(correctM)
                if (first == 0L && third > 0) append(third).append("s")
            }
        }
    }
}