package io.codelabs.xhandieshub.data

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cart(
    override val key: String,
    val product: String,    // Reference to product in database
    val pending: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
) : BaseModel {
    constructor() : this("", "")
}