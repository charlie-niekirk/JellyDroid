package me.cniekirk.jellydroid.core.data.mapping

import me.cniekirk.jellydroid.core.model.CollectionKind
import me.cniekirk.jellydroid.core.model.UserView
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.CollectionType

fun BaseItemDto.toUserView(serverUrl: String): UserView? {
    val collectionKind = when (collectionType) {
        CollectionType.TVSHOWS -> CollectionKind.SERIES
        CollectionType.MOVIES -> CollectionKind.MOVIES
        else -> null
    }

    return if (collectionKind == null) {
        null
    } else {
        return UserView(
            id = id.toString(),
            parentId = parentId.toString(),
            name = name ?: "",
            path = path ?: "",
            imageUrl = "$serverUrl/Items/$id/Images/Primary",
            // TODO: Use primaryImageAspectRatio
            aspectRatio = 1.7,
            collectionKind = collectionKind
        )
    }
}
