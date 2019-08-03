package io.codelabs.xhandieshub.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import io.codelabs.sdk.glide.GlideApp
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.layoutInflater
import io.codelabs.xhandieshub.model.Food

/**
 * Adapter for the home page products view pager
 */
class HomePagerAdapter(private val context: Context) : PagerAdapter() {
    @Nullable
    var pageOne: View? = null
    @Nullable
    var pageTwo: View? = null
    @Nullable
    var pageThree: View? = null
    @Nullable
    var pageFour: View? = null

    private val chefsPick = mutableListOf<Food>().apply {
        add(
            Food(
                "OfNxGeumsEYkkfKsfgzx",
                "Samosa",
                context.getString(R.string.lorem),
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/xhandieshub.appspot.com/o/Aloo-Samosa-1.jpg?alt=media&token=cbedd531-f2af-46cf-859c-f6f9f1dc7e4a"
            )
        )
        add(
            Food(
                "OfNxGeumsEYkkfKsfgzx",
                "",
                context.getString(R.string.lorem),
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/xhandieshub.appspot.com/o/Aloo-Samosa-1.jpg?alt=media&token=cbedd531-f2af-46cf-859c-f6f9f1dc7e4a"
            )
        )
        add(
            Food(
                "OfNxGeumsEYkkfKsfgzx",
                "",
                context.getString(R.string.lorem),
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/xhandieshub.appspot.com/o/Aloo-Samosa-1.jpg?alt=media&token=cbedd531-f2af-46cf-859c-f6f9f1dc7e4a"
            )
        )
        add(
            Food(
                "OfNxGeumsEYkkfKsfgzx",
                "",
                context.getString(R.string.lorem),
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/xhandieshub.appspot.com/o/Aloo-Samosa-1.jpg?alt=media&token=cbedd531-f2af-46cf-859c-f6f9f1dc7e4a"
            )
        )
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = 4

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) =
        container.removeView(`object` as View)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val v = getPage(container, position)
        container.addView(v)
        return v
    }

    private fun getPage(container: ViewGroup, position: Int): View {
        return when (position) {
            0 -> {
                if (pageOne == null) {
                    pageOne = context.layoutInflater.inflate(R.layout.pager_page, container, false)
                    val url =
                        "https://firebasestorage.googleapis.com/v0/b/xhandieshub.appspot.com/o/Aloo-Samosa-1.jpg?alt=media&token=cbedd531-f2af-46cf-859c-f6f9f1dc7e4a"
                    pageOne?.findViewById<TextView>(R.id.item_name)?.text = "Samosa"

                    GlideApp.with(context)
                        .asBitmap()
                        .load(url)
                        .placeholder(R.color.content_placeholder)
                        .error(R.color.content_placeholder)
                        .transition(BitmapTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(pageOne!!.findViewById(R.id.item_image))
                }
                pageOne!!
            }

            1 -> {
                if (pageTwo == null) {
                    pageTwo = context.layoutInflater.inflate(R.layout.pager_page, container, false)
                    val url =
                        "https://firebasestorage.googleapis.com/v0/b/xhandieshub.appspot.com/o/Aloo-Samosa-1.jpg?alt=media&token=cbedd531-f2af-46cf-859c-f6f9f1dc7e4a"
                    pageTwo?.findViewById<TextView>(R.id.item_name)?.text = "Samosa"

                    GlideApp.with(context)
                        .asBitmap()
                        .load(url)
                        .placeholder(R.color.content_placeholder)
                        .error(R.color.content_placeholder)
                        .transition(BitmapTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(pageTwo!!.findViewById(R.id.item_image))

                }
                pageTwo!!
            }

            2 -> {
                if (pageThree == null) {
                    pageThree =
                        context.layoutInflater.inflate(R.layout.pager_page, container, false)
                    pageThree?.findViewById<TextView>(R.id.item_name)?.text = "Samosa"
                    val url =
                        "https://firebasestorage.googleapis.com/v0/b/xhandieshub.appspot.com/o/Aloo-Samosa-1.jpg?alt=media&token=cbedd531-f2af-46cf-859c-f6f9f1dc7e4a"

                    GlideApp.with(context)
                        .asBitmap()
                        .load(url)
                        .placeholder(R.color.content_placeholder)
                        .error(R.color.content_placeholder)
                        .transition(BitmapTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(pageThree!!.findViewById(R.id.item_image))
                }
                pageThree!!
            }

            else -> {
                if (pageFour == null) {
                    pageFour = context.layoutInflater.inflate(R.layout.pager_page, container, false)
                    pageFour?.findViewById<TextView>(R.id.item_name)?.text = "Samosa"
                    val url =
                        "https://firebasestorage.googleapis.com/v0/b/xhandieshub.appspot.com/o/Aloo-Samosa-1.jpg?alt=media&token=cbedd531-f2af-46cf-859c-f6f9f1dc7e4a"

                    GlideApp.with(context)
                        .asBitmap()
                        .load(url)
                        .placeholder(R.color.content_placeholder)
                        .error(R.color.content_placeholder)
                        .transition(BitmapTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(pageFour!!.findViewById(R.id.item_image))
                }
                pageFour!!
            }
        }
    }


}