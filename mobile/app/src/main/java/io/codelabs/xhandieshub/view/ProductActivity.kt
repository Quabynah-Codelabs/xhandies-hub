package io.codelabs.xhandieshub.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import io.codelabs.sdk.util.debugLog
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.HubBaseActivity
import io.codelabs.xhandieshub.databinding.ActivityProductBinding

class ProductActivity : HubBaseActivity() {
    private lateinit var binding: ActivityProductBinding

    companion object {
        const val EXTRA_PRODUCT = "EXTRA_PRODUCT"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)

        if (intent.hasExtra(EXTRA_PRODUCT)) {
            binding.product = intent.getParcelableExtra(EXTRA_PRODUCT)
            debugLog(binding.product)
        }
    }
}