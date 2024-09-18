package me.cniekirk.jellydroid.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Server(
    @PrimaryKey val serverId: String,
    @ColumnInfo("base_url") val baseUrl: String,
    @ColumnInfo("name") val name: String,
)