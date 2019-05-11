package io.codelabs.xhandieshub.data

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import io.codelabs.sdk.glide.GlideApp
import io.codelabs.xhandieshub.R
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

    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(imageView: ImageView, imageUrl: String) {
            GlideApp.with(imageView.context)
                .load(imageUrl)
                .placeholder(R.color.content_placeholder)
                .error(R.color.content_placeholder)
                .into(imageView)
        }
    }
}