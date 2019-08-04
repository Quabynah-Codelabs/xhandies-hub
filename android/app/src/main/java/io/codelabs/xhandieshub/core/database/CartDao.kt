package io.codelabs.xhandieshub.core.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.codelabs.xhandieshub.model.Cart

/**
 * Data Access Object for [Cart] table
 */
@Dao
interface CartDao : BaseDao<Cart> {

    /**
     * Get the current user's shopping cart information
     */
    @Query("SELECT * FROM carts WHERE userId = :uid ORDER BY timestamp DESC")
    fun getShoppingCart(uid: String?): LiveData<MutableList<Cart>>

}