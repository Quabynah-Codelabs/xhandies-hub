package io.codelabs.xhandieshub.viewmodel.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.codelabs.xhandieshub.core.Utils
import io.codelabs.xhandieshub.core.common.QueryLiveData
import io.codelabs.xhandieshub.core.database.CartDao
import io.codelabs.xhandieshub.core.database.FoodDao
import io.codelabs.xhandieshub.core.prefs.AppPreferences
import io.codelabs.xhandieshub.model.Food
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodRepository private constructor(
    private val db: FirebaseFirestore,
    private val foodDao: FoodDao,
    private val cartDao: CartDao,
    private val prefs: AppPreferences
) {

    private val foodQuery
        get() = db.collection(Utils.FOODS_COLLECTION).orderBy(
            "key",
            Query.Direction.DESCENDING
        ).limit(50L)

    /**
     * Get all foods from the database
     */
    fun getAllFoods(): QueryLiveData<Food> = QueryLiveData(foodQuery, foodDao, Food::class.java)

    fun getAllLocalFoods(): LiveData<MutableList<Food>> = foodDao.getAllFoods()

    /**
     * Get a single [Food] item by [key]
     */
    fun getFoodByKey(key: String): LiveData<Food> = foodDao.getFoodByKey(key)

    suspend fun addToCart(food: Food) = withContext(Dispatchers.IO) {

    }

    suspend fun removeFromCart(food: Food) = withContext(Dispatchers.IO) {

    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {

    }


    companion object {
        @Volatile
        private var instance: FoodRepository? = null

        fun getInstance(
            db: FirebaseFirestore,
            dao: FoodDao,
            cartDao: CartDao,
            preferences: AppPreferences
        ): FoodRepository =
            instance ?: synchronized(this) {
                instance ?: FoodRepository(db, dao, cartDao, preferences).also { instance = it }
            }
    }
}