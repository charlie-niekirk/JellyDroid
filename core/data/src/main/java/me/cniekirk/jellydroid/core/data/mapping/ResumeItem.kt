package me.cniekirk.jellydroid.core.data.mapping

import me.cniekirk.jellydroid.core.domain.model.ResumeItem
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind

const val PLAYED_REDUCTION_FACTOR = 100

fun BaseItemDto.toResumeItem(serverUrl: String?): ResumeItem {
    return ResumeItem(
        id = id.toString(),
        name = if (type == BaseItemKind.EPISODE) seriesName ?: "" else name ?: "",
        imageUrl = if (type == BaseItemKind.MOVIE) {
            "$serverUrl/Items/$id/Images/Backdrop"
        } else {
            "$serverUrl/Items/$id/Images/Primary"
        },
        aspectRatio = 1.7,
        playedPercentage = userData?.playedPercentage?.div(PLAYED_REDUCTION_FACTOR)?.toFloat() ?: 0f
    )
}
