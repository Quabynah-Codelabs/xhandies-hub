package io.codelabs.xhandieshub.core.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 * Base interface for all Data Access Object interfaces for the Room Persistence Library
 */
interface BaseDao<T> {

    @Insert
    fun insertItem(item: T)

    @Update
    fun updateItem(vararg items: T)

    @Delete
    fun deleteItem(vararg items: T)

}