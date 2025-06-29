package me.cniekirk.jellydroid.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import me.cniekirk.jellydroid.core.database.dao.ServerDao
import me.cniekirk.jellydroid.core.database.dao.UserDao
import me.cniekirk.jellydroid.core.database.entity.Server
import me.cniekirk.jellydroid.core.database.entity.UserDto

@Database(entities = [Server::class, UserDto::class], version = 1)
abstract class JellydroidDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerDao
    abstract fun userDao(): UserDao
}
