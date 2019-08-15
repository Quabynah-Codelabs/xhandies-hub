package io.codelabs.xhandieshub.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import io.codelabs.xhandieshub.core.Utils
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "foods")
@Parcelize
data class Food(
    @PrimaryKey
    val key: String,
    var name: String,
    var desc: String? = null,
    var imageUrl: String? = null,
    var category: String = FoodCategory.CONTINENTAL,
    var price: Double = 1.00,
    var quantity: Int = 100,
    var restaurant: String? = Utils.DEFAULT_RESTAURANT
) : Model {

    @Ignore
    // No-Argument constructor needed for serialization from Firebase Firestore
    constructor() : this("", "")

    object FoodCategory {
        const val CONTINENTAL = "Continental Dish"
        const val LOCAL = "Local Dish"
        const val OTHER = "Other Dish"
    }
}