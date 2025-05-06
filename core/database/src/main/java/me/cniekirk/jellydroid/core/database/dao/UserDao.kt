package me.cniekirk.jellydroid.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import me.cniekirk.jellydroid.core.database.entity.UserDto

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg user: UserDto)

    @Delete
    fun delete(user: UserDto)
}
