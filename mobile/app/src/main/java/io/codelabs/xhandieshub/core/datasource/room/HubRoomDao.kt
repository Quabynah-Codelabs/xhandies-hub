package io.codelabs.xhandieshub.core.datasource.room

import androidx.lifecycle.LiveData
import androidx.room.*
import io.codelabs.xhandieshub.data.User

/**
 * [Room] data access object
 */
@Dao
interface HubRoomDao {

    @Query("SELECT * FROM users WHERE `key` IS :key")
    fun getCurrentUser(key: String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(vararg user: User)

    @Delete
    fun deleteUser(vararg user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: User)
}