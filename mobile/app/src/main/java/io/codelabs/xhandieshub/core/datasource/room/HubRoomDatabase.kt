package io.codelabs.xhandieshub.core.datasource.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.codelabs.xhandieshub.BuildConfig
import io.codelabs.xhandieshub.core.util.Constants
import io.codelabs.xhandieshub.data.User

@Database(entities = [User::class], version = BuildConfig.VERSION_CODE, exportSchema = true)
abstract class HubRoomDatabase : RoomDatabase() {

    abstract fun dao(): HubRoomDao

    companion object {
        @Volatile
        private var instance: HubRoomDatabase? = null

        fun getInstance(context: Context): HubRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, HubRoomDatabase::class.java, Constants.ROOM_DATABASE)
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
        }
    }

}