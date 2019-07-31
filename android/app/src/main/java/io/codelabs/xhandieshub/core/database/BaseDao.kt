package io.codelabs.xhandieshub.core.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Base interface for all Data Access Object interfaces for the Room Persistence Library
 */
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateItem(vararg items: T)

    @Delete
    fun deleteItem(vararg items: T)

}