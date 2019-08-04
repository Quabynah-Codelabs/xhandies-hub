package io.codelabs.xhandieshub.view

import android.os.Bundle
import androidx.lifecycle.Observer
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.core.debugger
import io.codelabs.xhandieshub.viewmodel.FoodViewModel
import kotlinx.android.synthetic.main.activity_cart.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Shopping Cart Screen
 * Shows all items in a user's shopping cart
 */
class CartActivity : BaseActivity() {
    private val foodViewModel by viewModel<FoodViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Setup toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        foodViewModel.cart.observe(this, Observer {cartList ->
            debugger("Shopping cart: $cartList")
        })
    }
}