package io.codelabs.xhandieshub.view.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import io.codelabs.sdk.glide.GlideApp
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.layoutInflater
import io.codelabs.xhandieshub.model.Food
import io.codelabs.xhandieshub.view.FoodDetailsActivity
import kotlinx.android.synthetic.main.pager_page.view.*

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
                "Rice with Kontomire Stew",
                context.getString(R.string.lorem),
                imageUrl = "https://media.timeout.com/images/102963971/630/472/image.jpg"
            )
        )
        add(
            Food(
                "OfNxGeumsEYkkfKsfgzx",
                "Fante Fante with Banku",
                context.getString(R.string.lorem),
                imageUrl = "https://i.ytimg.com/vi/4C6fnG6qUVY/maxresdefault.jpg"
            )
        )
        add(
            Food(
                "OfNxGeumsEYkkfKsfgzx",
                "Plantain with Kontomire Stew",
                context.getString(R.string.lorem),
                imageUrl = "https://cdn.shopify.com/s/files/1/0760/0339/files/ampesi_large.jpg?5862185721686863705"
            )
        )
        add(
            Food(
                "OfNxGeumsEYkkfKsfgzx",
                "Banku with Tilapia",
                context.getString(R.string.lorem),
                imageUrl = "https://www.primenewsghana.com/images/banku_1.jpg"
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
                    pageOne?.item_name?.text = chefsPick[0].name

                    GlideApp.with(context)
                        .asBitmap()
                        .load(chefsPick[0].imageUrl)
                        .placeholder(R.color.content_placeholder)
                        .error(R.color.content_placeholder)
                        .transition(BitmapTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(pageOne!!.item_image)

                    pageOne?.item_image?.setOnClickListener {
                        context.startActivity(
                            Intent(
                                context,
                                FoodDetailsActivity::class.java
                            ).apply {
                                putExtras(
                                    bundleOf(
                                        Pair(FoodDetailsActivity.FOOD, chefsPick[0])
                                    )
                                )
                            })
                    }
                }
                pageOne!!
            }

            1 -> {
                if (pageTwo == null) {
                    pageTwo = context.layoutInflater.inflate(R.layout.pager_page, container, false)
                    pageTwo?.item_name?.text = chefsPick[1].name

                    GlideApp.with(context)
                        .asBitmap()
                        .load(chefsPick[1].imageUrl)
                        .placeholder(R.color.content_placeholder)
                        .error(R.color.content_placeholder)
                        .transition(BitmapTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(pageTwo!!.item_image)

                    pageTwo?.item_image?.setOnClickListener {
                        context.startActivity(
                            Intent(
                                context,
                                FoodDetailsActivity::class.java
                            ).apply {
                                putExtras(
                                    bundleOf(
                                        Pair(FoodDetailsActivity.FOOD, chefsPick[1])
                                    )
                                )
                            })
                    }
                }
                pageTwo!!
            }

            2 -> {
                if (pageThree == null) {
                    pageThree =
                        context.layoutInflater.inflate(R.layout.pager_page, container, false)
                    pageThree?.item_name?.text = chefsPick[2].name

                    GlideApp.with(context)
                        .asBitmap()
                        .load(chefsPick[2].imageUrl)
                        .placeholder(R.color.content_placeholder)
                        .error(R.color.content_placeholder)
                        .transition(BitmapTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(pageThree!!.item_image)

                    pageThree?.item_image?.setOnClickListener {
                        context.startActivity(
                            Intent(
                                context,
                                FoodDetailsActivity::class.java
                            ).apply {
                                putExtras(
                                    bundleOf(
                                        Pair(FoodDetailsActivity.FOOD, chefsPick[2])
                                    )
                                )
                            })
                    }
                }
                pageThree!!
            }

            else -> {
                if (pageFour == null) {
                    pageFour = context.layoutInflater.inflate(R.layout.pager_page, container, false)
                    pageFour?.item_name?.text = chefsPick[3].name

                    GlideApp.with(context)
                        .asBitmap()
                        .load(chefsPick[3].imageUrl)
                        .placeholder(R.color.content_placeholder)
                        .error(R.color.content_placeholder)
                        .transition(BitmapTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(pageFour!!.item_image)

                    pageFour?.item_image?.setOnClickListener {
                        context.startActivity(
                            Intent(
                                context,
                                FoodDetailsActivity::class.java
                            ).apply {
                                putExtras(
                                    bundleOf(
                                        Pair(FoodDetailsActivity.FOOD, chefsPick[3])
                                    )
                                )
                            })
                    }

                }
                pageFour!!
            }
        }
    }


}