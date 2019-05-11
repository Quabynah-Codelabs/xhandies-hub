package io.codelabs.xhandieshub.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.HubBaseActivity
import io.codelabs.xhandieshub.databinding.ActivityCartBinding

class CartActivity : HubBaseActivity() {
    private lateinit var binding: ActivityCartBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart)
        setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

    }
}