package io.codelabs.xhandieshub.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Base [User] data model
 */
@Parcelize
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = false)
    override val key: String,
    var displayName: String?,
    var email: String?,
    var avatar: String?,
    var token: String? = null
) : BaseModel {

    @Ignore
    constructor() : this("", "", "", "")
}