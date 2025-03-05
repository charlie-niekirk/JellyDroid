package me.cniekirk.jellydroid.core.data.mapping

import me.cniekirk.jellydroid.core.model.LatestItem
import org.jellyfin.sdk.model.api.BaseItemDto

fun BaseItemDto.toLatestItem(serverUrl: String?): LatestItem {
    return LatestItem(
        id = id.toString(),
        name = name ?: "",
        imageUrl = "$serverUrl/Items/$id/Images/Primary"
    )
}
