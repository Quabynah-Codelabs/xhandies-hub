package io.codelabs.xhandieshub.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Cart(
    @PrimaryKey
    val key: String,
    var foodId: String,
    var quantity: Int = 0,
    var timestamp: Long = System.currentTimeMillis()
) : Parcelable {

    @Ignore
    constructor() : this("", "")
}