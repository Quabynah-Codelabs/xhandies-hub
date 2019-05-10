package io.codelabs.xhandieshub.data

import android.os.Parcelable

/**
 * Base data model class for all data models
 */
interface BaseModel : Parcelable {
    val key: String
}