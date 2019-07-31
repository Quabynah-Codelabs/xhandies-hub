package io.codelabs.xhandieshub.view

import android.os.Bundle
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_cart.*

/**
 * Shopping Cart Screen
 * Shows all items in a user's shopping cart
 */
class CartActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Setup toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}