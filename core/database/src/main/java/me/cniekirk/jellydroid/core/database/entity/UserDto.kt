package me.cniekirk.jellydroid.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserDto(
    @PrimaryKey val userId: String,
    @ColumnInfo("belongs_to_server_id") val belongsToServerId: String,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("access_token") val accessToken: String,
    @ColumnInfo("profile_image_url") val profileImageUrl: String
)
