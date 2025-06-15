package me.cniekirk.jellydroid.core.domain.model.servers

data class User(
    val userId: String,
    val belongsToServerId: String,
    val name: String,
    val accessToken: String,
    val profileImageUrl: String
)