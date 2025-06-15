package me.cniekirk.jellydroid.core.data.mapping

import me.cniekirk.jellydroid.core.database.relations.ServerWithUsers
import me.cniekirk.jellydroid.core.domain.model.servers.Server
import me.cniekirk.jellydroid.core.domain.model.servers.ServerAndUsers
import me.cniekirk.jellydroid.core.domain.model.servers.User

internal fun ServerWithUsers.toServerAndUsers(): ServerAndUsers =
    ServerAndUsers(
        server = Server(
            serverId = server.serverId,
            baseUrl = server.baseUrl,
            name = server.name
        ),
        users = users.map { user ->
            User(
                userId = user.userId,
                belongsToServerId = user.belongsToServerId,
                name = user.name,
                accessToken = user.accessToken,
                profileImageUrl = "${server.baseUrl}/Users/${user.userId}/Images/Primary"
            )
        }
    )
