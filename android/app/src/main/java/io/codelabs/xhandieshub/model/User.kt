package io.codelabs.xhandieshub.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "users")
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = false)
    val uid: String,
    val email: String,
    val username: String? = null,
    var timestamp: Long = System.currentTimeMillis(),
    var creditCard: String? = null,
    var cashBalance: Long? = -1L
) : Model {

    @Ignore
    // No-Argument constructor needed for serialization from Firebase Firestore
    constructor() : this("", "", "")
}