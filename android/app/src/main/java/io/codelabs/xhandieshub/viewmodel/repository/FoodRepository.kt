package io.codelabs.xhandieshub.viewmodel.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import io.codelabs.xhandieshub.core.Utils
import io.codelabs.xhandieshub.core.common.QueryLiveData
import io.codelabs.xhandieshub.core.database.CartDao
import io.codelabs.xhandieshub.core.database.FoodDao
import io.codelabs.xhandieshub.core.prefs.AppPreferences
import io.codelabs.xhandieshub.model.Cart
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

    suspend fun addToCart(food: Food, quantity: Int = 1) = withContext(Dispatchers.IO) {
        // Food document
        val foodDoc = db.document(String.format("%s/%s", Utils.FOODS_COLLECTION, food.key))

        // Create a new document
        val document = db.collection(String.format(Utils.CART_COLLECTION, prefs.uid)).document()

        // Cart
        val cart = Cart(document.id, food.key, prefs.uid!!, quantity)

        // Add to cart
        cartDao.insertItem(cart)

        // Await task to add item to cart
        Tasks.await(document.set(cart, SetOptions.merge()))

        // Reduce quantity
        db.runTransaction { t ->
            val newQty = t.get(foodDoc).toObject(Food::class.java)?.quantity?.minus(1) ?: 0
            t.update(foodDoc, "quantity", newQty)
            null
        }
        null
    }

    suspend fun removeFromCart(cart: Cart) = withContext(Dispatchers.IO) {
        // Remove cart item from local database
        cartDao.deleteItem(cart)

        // Remove from remote database
        Tasks.await(
            Tasks.await(
                db.collection(
                    String.format(
                        Utils.CART_COLLECTION,
                        prefs.uid
                    )
                ).document(cart.key).get()
            ).reference.delete()
        )
        null
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        // Get all cart items locally
        cartDao.getShoppingCartSnapshot(prefs.uid).forEach { cart ->
            // Delete item in list
            cartDao.deleteItem(cart)
        }

        // Get snapshot for user
        val snapshot =
            Tasks.await(db.collection(String.format(Utils.CART_COLLECTION, prefs.uid)).get())

        // Get all documents and delete each one at a time
        snapshot?.documents?.forEach { doc ->

            // Delete document from the remote database
            Tasks.await(doc.reference.delete())
        }

        null
    }

    fun getCart(): LiveData<MutableList<Cart>> = cartDao.getShoppingCart(prefs.uid)

    suspend fun getCartItem(foodId: String?): Boolean = withContext(Dispatchers.IO) {
        val cartItem = cartDao.getCartItem(foodId)
        cartItem == null
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