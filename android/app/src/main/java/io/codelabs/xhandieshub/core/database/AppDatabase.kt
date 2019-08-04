package io.codelabs.xhandieshub.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.codelabs.xhandieshub.core.Utils
import io.codelabs.xhandieshub.model.Cart
import io.codelabs.xhandieshub.model.Food
import io.codelabs.xhandieshub.model.User

@Database(entities = [User::class, Food::class, Cart::class], version = 5, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun foodDao(): FoodDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context, AppDatabase::class.java, Utils.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build().also { instance = it }

        }
    }

}