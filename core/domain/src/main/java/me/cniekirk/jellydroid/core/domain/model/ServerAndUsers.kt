package me.cniekirk.jellydroid.core.domain.model

data class User(
    val userId: String,
    val belongsToServerId: String,
    val name: String,
    val accessToken: String,
)

data class Server(
    val serverId: String,
    val baseUrl: String,
    val name: String,
)

data class ServerAndUsers(
    val server: Server,
    val users: List<User>
)
