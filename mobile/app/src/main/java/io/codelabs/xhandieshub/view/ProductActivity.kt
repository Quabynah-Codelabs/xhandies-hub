package io.codelabs.xhandieshub.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.toast
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.HubBaseActivity
import io.codelabs.xhandieshub.core.util.Constants
import io.codelabs.xhandieshub.data.Cart
import io.codelabs.xhandieshub.databinding.ActivityProductBinding

class ProductActivity : HubBaseActivity() {
    private lateinit var binding: ActivityProductBinding

    companion object {
        const val EXTRA_PRODUCT = "EXTRA_PRODUCT"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        if (intent.hasExtra(EXTRA_PRODUCT)) {
            binding.product = intent.getParcelableExtra(EXTRA_PRODUCT)
            debugLog(binding.product)
        }
    }

    fun addToCart(v: View?) {
        toast("Adding \"${binding.product?.name}\" to cart...")
        val document = firestore.collection(String.format(Constants.Database.CART, prefs.uid)).document(binding.product!!.key)
        document.set(Cart(binding.product!!.key, binding.product!!.key))
            .addOnCompleteListener {
                debugLog("Adding item to cart: ${it.isSuccessful}")
            }.addOnFailureListener { ex ->
                debugLog(ex.localizedMessage)
                toast("Failed to add \"${binding.product?.name}\" to your shopping cart", true)
            }
        finishAfterTransition()
    }
}