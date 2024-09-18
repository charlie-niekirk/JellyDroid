package me.cniekirk.jellydroid.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import me.cniekirk.jellydroid.core.database.entity.Server
import me.cniekirk.jellydroid.core.database.relations.ServerWithUsers

@Dao
interface ServerDao {

    @Transaction
    @Query("SELECT * FROM server")
    fun getAllServersWithUsers(): List<ServerWithUsers>

    @Query("SELECT * FROM server WHERE name LIKE '%' || :name  || '%'")
    fun getByName(name: String): List<Server>

    @Insert
    fun insertAll(vararg servers: Server)

    @Delete
    fun delete(server: Server)
}