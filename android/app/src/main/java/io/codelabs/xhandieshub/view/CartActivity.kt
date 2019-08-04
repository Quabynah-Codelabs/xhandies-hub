package io.codelabs.xhandieshub.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.codelabs.sdk.util.intentTo
import io.codelabs.sdk.util.toast
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.core.debugger
import io.codelabs.xhandieshub.core.payment.PaymentService
import io.codelabs.xhandieshub.databinding.ActivityCartBinding
import io.codelabs.xhandieshub.model.Cart
import io.codelabs.xhandieshub.view.adapter.CartAdapter
import io.codelabs.xhandieshub.viewmodel.FoodViewModel
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Shopping Cart Screen
 * Shows all items in a user's shopping cart
 */
class CartActivity : BaseActivity() {
    private lateinit var binding: ActivityCartBinding

    private val service by inject<PaymentService>()
    private val foodViewModel by viewModel<FoodViewModel>()
    private val carts = mutableListOf<Cart>()

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
            }
        })
    }

    fun makePayment(v: View?) {
        ioScope.launch {
            try {
                val response =
                    service.makePaymentAsync(PaymentService.PaymentRequest(prefs.uid!!, carts))
                        .await()

                // get response
                val apiResponse = response.body()
                if (apiResponse != null) {
                    debugger("Response from payment: ${apiResponse.message}")

                    if (apiResponse.error) {
                        showFailureDialog()
                    } else {
                        // Clear cart
                        foodViewModel.clearCart()
                        uiScope.launch {
                            toast("Payment was successful")
                            intentTo(TrackingActivity::class.java, true)
                        }
                    }
                } else {
                    debugger("Cannot make request to server. Response is null")
                    showFailureDialog()
                }
            } catch (e: Exception) {
                debugger(e.localizedMessage)
                uiScope.launch {
                    toast("Unable to make payment")
                    showFailureDialog()
                }
            }
        }
    }

    private fun showFailureDialog() {
        MaterialAlertDialogBuilder(this@CartActivity).apply {
            setTitle("Payment cancelled")
            setMessage("Xhandie\'s Hub cannot complete your payment. There seems to be a problem on our side. We will get back to you soon")
            setOnDismissListener { finishAfterTransition() }
            setPositiveButton("Dismiss") { dialogInterface, _ -> dialogInterface.dismiss() }
            show()
        }
    }
}