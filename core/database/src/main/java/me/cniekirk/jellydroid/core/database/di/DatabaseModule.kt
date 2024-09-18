package me.cniekirk.jellydroid.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.cniekirk.jellydroid.core.database.JellydroidDatabase
import me.cniekirk.jellydroid.core.database.dao.ServerDao
import me.cniekirk.jellydroid.core.database.dao.UserDao
import javax.inject.Singleton

const val DATABASE_NAME = "jellydroid_app_db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideJellydroidDatabase(@ApplicationContext context: Context): JellydroidDatabase {
        return Room.databaseBuilder(
            context,
            JellydroidDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideServerDao(jellydroidDatabase: JellydroidDatabase): ServerDao = jellydroidDatabase.serverDao()

    @Singleton
    @Provides
    fun provideUserDao(jellydroidDatabase: JellydroidDatabase): UserDao = jellydroidDatabase.userDao()
}