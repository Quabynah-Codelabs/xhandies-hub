package io.codelabs.xhandieshub.view

import android.app.Activity
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.codelabs.sdk.util.intentTo
import io.codelabs.sdk.util.toast
import io.codelabs.util.AnimUtils
import io.codelabs.widget.BottomSheet
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.core.debugger
import io.codelabs.xhandieshub.core.location.GPSTracker
import io.codelabs.xhandieshub.core.payment.PaymentService
import io.codelabs.xhandieshub.model.Cart
import io.codelabs.xhandieshub.model.Food
import io.codelabs.xhandieshub.viewmodel.FoodViewModel
import io.codelabs.xhandieshub.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.order_details.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class OrderActivity : BaseActivity() {

    private val service by inject<PaymentService>()
    private val carts = mutableListOf<Cart>()
    private val foodViewModel by viewModel<FoodViewModel>()
    private val viewModel by viewModel<UserViewModel>()
    private var appBarElevation: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        if (intent.hasExtra(EXTRA_CART)) {
            carts.addAll(intent.getParcelableArrayListExtra<Cart>(EXTRA_CART))
            debugger("Intent extras: $carts")
            checkout.isEnabled = carts.isNotEmpty()
            getTotalAmount(carts)
            getUserInformation()
        }

        appBarElevation = resources.getDimension(R.dimen.z_app_bar)
        bottom_sheet.registerCallback(object : BottomSheet.Callbacks() {
            override fun onSheetDismissed() {
                // After a drag dismiss, finish without the shared element return transition as
                // it no longer makes sense.  Let the launching window know it's a drag dismiss so
                // that it can restore any UI used as an entering shared element
                setResult(Activity.RESULT_OK)
                finish()
            }
        })

        scroll_container.setListener { scrollY ->
            if (scrollY != 0
                && sheet_title.translationZ != appBarElevation
            ) {
                sheet_title.animate()
                    .translationZ(appBarElevation)
                    .setStartDelay(0L)
                    .setDuration(80L)
                    .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(this@OrderActivity))
                    .start()
            } else if (scrollY == 0 && sheet_title.translationZ == appBarElevation) {
                sheet_title.animate()
                    .translationZ(0f)
                    .setStartDelay(0L)
                    .setDuration(80L)
                    .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(this@OrderActivity))
                    .start()
            }
        }

        bottom_sheet.setOnClickListener { onBackPressed() }
    }

    private fun getUserInformation() {
        viewModel.currentUser.observe(this@OrderActivity, Observer { user ->
            buyer.summary = user?.username ?: user?.email
            val tracker = GPSTracker(this@OrderActivity, null)

            ioScope.launch {
                with(Geocoder(this@OrderActivity, Locale.getDefault())) {
                    try {
                        val addressLine = this.getFromLocation(
                            tracker.latitude,
                            tracker.longitude,
                            1
                        )[0].getAddressLine(0)

                        uiScope.launch {
                            deliver_to.summary = addressLine
                        }

                    } catch (e: Exception) {
                        debugger(e.localizedMessage)
                        uiScope.launch {
                            deliver_to.summary = "Cannot get location address"
                        }
                    }
                }
            }

        })
    }

    private fun getTotalAmount(cartList: MutableList<Cart>) {
        foodViewModel.getAllLocalFoods().observe(this@OrderActivity, Observer {
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
            order_total.text =
                String.format(getString(R.string.formatted_total_amount), totalAmount)
        })
    }

    fun checkoutPayment(v: View?) {
        TransitionManager.beginDelayedTransition(bottom_sheet_content)
        loading.visibility = View.VISIBLE

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
        TransitionManager.beginDelayedTransition(bottom_sheet_content)
        loading.visibility = View.GONE

        MaterialAlertDialogBuilder(this@OrderActivity).apply {
            setTitle("Payment was unsuccessful")
            setMessage("${getString(R.string.default_app_name)} cannot complete your payment. There seems to be a problem on our side. We will get back to you soon")
            setOnDismissListener { finishAfterTransition() }
            setPositiveButton("Dismiss") { dialogInterface, _ -> dialogInterface.dismiss() }
            show()
        }
    }

    companion object {
        const val EXTRA_CART = "CARTS"
    }

    fun choosePayment(view: View) {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(getString(R.string.confirm_trans_hint))
            setItems(
                arrayOf(
                    getString(R.string.momo),
                    getString(R.string.visa)
                )
            ) { dialogInterface, i ->
                dialogInterface.dismiss()

                when (i) {
                    0 -> {

                    }

                    1 -> {

                    }
                }
            }
            setPositiveButton("Cancel") { dialogInterface, _ -> dialogInterface.dismiss() }

            show()
        }
    }
}