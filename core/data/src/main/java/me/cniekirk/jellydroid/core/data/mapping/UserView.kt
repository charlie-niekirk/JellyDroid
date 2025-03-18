package me.cniekirk.jellydroid.core.data.mapping

import me.cniekirk.jellydroid.core.model.UserView
import org.jellyfin.sdk.model.api.BaseItemDto

fun BaseItemDto.toUserView(serverUrl: String): UserView =
    UserView(
        id = id.toString(),
        parentId = parentId.toString(),
        name = name ?: "",
        path = path ?: "",
        imageUrl = "$serverUrl/Items/$id/Images/Primary",
        // TODO: Use primaryImageAspectRatio
        aspectRatio = 1.7
    )
