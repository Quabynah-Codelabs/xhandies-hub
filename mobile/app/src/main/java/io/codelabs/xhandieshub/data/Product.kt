package io.codelabs.xhandieshub.data

import kotlinx.android.parcel.Parcelize

/**
 * Products data class
 */
@Parcelize
data class Product(
    override val key: String,
    var name: String,
    var price: String,
    var description: String? = null,
    var image: String? = null,
    var category: String? = Category.BEVERAGE,
    var timestamp: Long = System.currentTimeMillis()
) : BaseModel {

    constructor() : this("", "", "")

    object Category {
        const val BEVERAGE = "beverage"
        const val FOOD = "food"
    }
}