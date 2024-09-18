package me.cniekirk.jellydroid.core.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import me.cniekirk.jellydroid.core.database.entity.Server
import me.cniekirk.jellydroid.core.database.entity.User

data class ServerWithUsers(
    @Embedded val server: Server,
    @Relation(
        parentColumn = "serverId",
        entityColumn = "belongs_to_server_id"
    )
    val users: List<User>
)
