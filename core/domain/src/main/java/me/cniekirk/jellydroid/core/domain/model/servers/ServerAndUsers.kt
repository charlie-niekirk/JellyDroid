package me.cniekirk.jellydroid.core.domain.model.servers

data class ServerAndUsers(
    val server: Server,
    val users: List<User>
)