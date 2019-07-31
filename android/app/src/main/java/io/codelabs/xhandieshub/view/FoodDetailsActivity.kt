package io.codelabs.xhandieshub.view

import android.os.Bundle
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity

class FoodDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)
    }

    companion object {
        const val FOOD = "food"
    }
}