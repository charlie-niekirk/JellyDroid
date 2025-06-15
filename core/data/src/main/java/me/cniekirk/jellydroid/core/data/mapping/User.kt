package me.cniekirk.jellydroid.core.data.mapping

import me.cniekirk.jellydroid.core.database.entity.UserDto
import me.cniekirk.jellydroid.core.domain.model.servers.User
import org.jellyfin.sdk.model.api.AuthenticationResult

@Suppress("ReturnCount")
fun AuthenticationResult.toUserDto(baseUrl: String): UserDto? {
    return UserDto(
        userId = this.user?.id.toString(),
        belongsToServerId = this.serverId ?: return null,
        name = this.user?.name ?: return null,
        accessToken = this.accessToken ?: return null,
        profileImageUrl = "$baseUrl/Users/${this.user?.id}/Images/Primary"
    )
}

fun UserDto.toUser(baseUrl: String): User =
    User(
        userId = userId,
        belongsToServerId = belongsToServerId,
        name = name,
        accessToken = accessToken,
        profileImageUrl = "$baseUrl/Users/$userId/Images/Primary"
    )
