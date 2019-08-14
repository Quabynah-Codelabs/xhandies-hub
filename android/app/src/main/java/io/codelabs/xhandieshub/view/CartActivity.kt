package io.codelabs.xhandieshub.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.core.debugger
import io.codelabs.xhandieshub.databinding.ActivityCartBinding
import io.codelabs.xhandieshub.model.Cart
import io.codelabs.xhandieshub.model.Food
import io.codelabs.xhandieshub.view.adapter.CartAdapter
import io.codelabs.xhandieshub.viewmodel.FoodViewModel
import kotlinx.android.synthetic.main.activity_cart.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Shopping Cart Screen
 * Shows all items in a user's shopping cart
 */
class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding
    private val foodViewModel by viewModel<FoodViewModel>()
    private val carts = arrayListOf<Cart>()
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart)

        // Setup toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Setup recyclerview
        adapter = CartAdapter(this, foodViewModel)
        cart_list.adapter = adapter
        cart_list.itemAnimator = DefaultItemAnimator()
        cart_list.setHasFixedSize(false)

        // Kick-off initial load
        foodViewModel.cart.observe(this, Observer { cartList ->
            debugger("Shopping cart: ${cartList?.size}")
            binding.isEmptyCart = cartList.isEmpty()
            if (cartList != null) {
                carts.clear()
                carts.addAll(cartList)
                adapter.addCarts(cartList)
                getTotalAmount(cartList)
            }
        })
    }

    private fun getTotalAmount(cartList: MutableList<Cart>) {
        foodViewModel.getAllLocalFoods().observe(this@CartActivity, Observer {
            // Show total amount
            var totalAmount = 0.00
            val foods = mutableListOf<Food>()

            cartList.forEach { dish ->
                foods.addAll(it.filter { dish.foodId == it.key })
            }

            foods.forEach { foodItem ->
                totalAmount += foodItem.price
            }

            // Set amount text
            total_amount.text =
                String.format(getString(R.string.formatted_total_amount), totalAmount)
        })
    }

    fun makePayment(v: View?) {
        startActivity(Intent(this@CartActivity, OrderActivity::class.java).apply {
            putParcelableArrayListExtra(OrderActivity.EXTRA_CART, carts)
        })
    }

}