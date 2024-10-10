package me.cniekirk.jellydroid.core.data.mapping

import me.cniekirk.jellydroid.core.model.UserView
import org.jellyfin.sdk.model.api.BaseItemDto

fun BaseItemDto.toUserView(serverUrl: String): UserView =
    UserView(
        id = id.toString(),
        parentId = parentId.toString(),
        name = name ?: "",
        path = path ?: "",
        type = type.serialName,
        imageUrl = "$serverUrl/Items/$id/Images/Primary",
        aspectRatio = /*primaryImageAspectRatio ?: */1.7
    )