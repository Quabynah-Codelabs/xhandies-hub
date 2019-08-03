package io.codelabs.xhandieshub.core.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.codelabs.xhandieshub.model.Food

/**
 * Data Access Object for [Food] table
 */
@Dao
interface FoodDao : BaseDao<Food> {

    @Query("SELECT * FROM foods WHERE `key` = :key")
    fun getFoodByKey(key: String): LiveData<Food>

    @Query("SELECT * FROM foods ORDER BY price DESC")
    fun getAllFoods(): LiveData<MutableList<Food>>

}