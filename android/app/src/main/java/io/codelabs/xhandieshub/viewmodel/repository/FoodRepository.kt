package io.codelabs.xhandieshub.viewmodel.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.codelabs.xhandieshub.core.database.FoodDao
import io.codelabs.xhandieshub.model.Food

class FoodRepository private constructor(
    private val db: FirebaseFirestore,
    private val dao: FoodDao
) {

    /**
     * Get all foods from the database
     */
    fun getAllFoods(): LiveData<MutableList<Food>> = dao.getAllFoods()

    /**
     * Get a single [Food] item by [key]
     */
    fun getFoodByKey(key: String): LiveData<Food> = dao.getFoodByKey(key)


    companion object {
        @Volatile
        private var instance: FoodRepository? = null

        fun getInstance(db: FirebaseFirestore, dao: FoodDao): FoodRepository =
            instance ?: synchronized(this) {
                instance ?: FoodRepository(db, dao).also { instance = it }
            }
    }
}