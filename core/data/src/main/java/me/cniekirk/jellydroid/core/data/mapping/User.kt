package me.cniekirk.jellydroid.core.data.mapping

import me.cniekirk.jellydroid.core.database.entity.UserDto
import me.cniekirk.jellydroid.core.domain.model.servers.User
import org.jellyfin.sdk.model.api.AuthenticationResult

fun AuthenticationResult.toUserDto(): UserDto {
    return UserDto(
        userId = this.user?.id.toString(),
        belongsToServerId = this.serverId ?: "",
        name = this.user?.name ?: "",
        accessToken = this.accessToken ?: ""
    )
}

fun UserDto.toUser(): User =
    User(
        userId = userId,
        belongsToServerId = belongsToServerId,
        name = name,
        accessToken = accessToken
    )
