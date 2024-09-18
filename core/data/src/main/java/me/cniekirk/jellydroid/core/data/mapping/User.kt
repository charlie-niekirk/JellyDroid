package me.cniekirk.jellydroid.core.data.mapping

import me.cniekirk.jellydroid.core.database.entity.User
import org.jellyfin.sdk.model.api.AuthenticationResult

fun AuthenticationResult.toUser(): User {
    return User(
        userId = this.user?.id.toString(),
        belongsToServerId = this.serverId ?: "",
        name = this.user?.name ?: "",
        accessToken = this.accessToken ?: ""
    )
}