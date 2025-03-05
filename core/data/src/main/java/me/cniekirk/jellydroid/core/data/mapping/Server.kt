package me.cniekirk.jellydroid.core.data.mapping

import me.cniekirk.jellydroid.core.database.entity.Server
import org.jellyfin.sdk.model.api.ServerDiscoveryInfo

fun ServerDiscoveryInfo.toServer(): Server {
    return Server(
        this.id,
        this.address,
        this.name
    )
}
