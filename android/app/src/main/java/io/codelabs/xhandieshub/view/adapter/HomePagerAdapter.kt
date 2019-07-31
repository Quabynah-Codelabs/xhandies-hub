package io.codelabs.xhandieshub.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.viewpager.widget.PagerAdapter
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.layoutInflater

class HomePagerAdapter(private val context: Context) : PagerAdapter() {
    @Nullable
    var pageOne: View? = null
    @Nullable
    var pageTwo: View? = null
    @Nullable
    var pageThree: View? = null
    @Nullable
    var pageFour: View? = null


    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = 4

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        container.removeView(`object` as View)
    }

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
                }
                pageOne!!
            }

            1 -> {
                if (pageTwo == null) {
                    pageTwo = context.layoutInflater.inflate(R.layout.pager_page, container, false)
                }
                pageTwo!!
            }

            2 -> {
                if (pageThree == null) {
                    pageThree =
                        context.layoutInflater.inflate(R.layout.pager_page, container, false)
                }
                pageThree!!
            }

            else -> {
                if (pageFour == null) {
                    pageFour = context.layoutInflater.inflate(R.layout.pager_page, container, false)
                }
                pageFour!!
            }
        }
    }


}