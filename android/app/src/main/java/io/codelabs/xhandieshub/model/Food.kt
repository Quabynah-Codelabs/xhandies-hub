package io.codelabs.xhandieshub.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import io.codelabs.sdk.glide.GlideApp
import io.codelabs.xhandieshub.R
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
    var restaurant: String? = null
) : Model {

    @Ignore
    // No-Argument constructor needed for serialization from Firebase Firestore
    constructor() : this("", "")

    object FoodCategory {
        const val CONTINENTAL = "continental"
        const val LOCAL = "local"
        const val OTHER = "other"
    }

    companion object {

        @JvmStatic
        @BindingAdapter("foodImage")
        fun loadFoodImage(view: ImageView, url: String?) {
            GlideApp.with(view.context)
                .asBitmap()
                .load(url)
                .placeholder(R.color.content_placeholder)
                .error(R.color.content_placeholder)
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(view)
        }

    }
}