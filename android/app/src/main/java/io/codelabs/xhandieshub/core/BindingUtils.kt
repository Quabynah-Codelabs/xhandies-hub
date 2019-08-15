package io.codelabs.xhandieshub.core

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.target.Target
import io.codelabs.sdk.glide.GlideApp
import io.codelabs.xhandieshub.R

object BindingUtils {
    @JvmStatic
    @BindingAdapter("foodImage")
    fun loadFoodImage(view: ImageView, url: String?) {
        GlideApp.with(view.context)
            .asBitmap()
            .load(url)
            .override(Target.SIZE_ORIGINAL)
            .placeholder(R.color.content_placeholder)
            .error(R.color.content_placeholder)
            .transition(BitmapTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(view)
    }
}