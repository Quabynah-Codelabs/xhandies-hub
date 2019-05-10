package io.codelabs.xhandieshub.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.HubBaseActivity
import io.codelabs.xhandieshub.databinding.ActivityHomeBinding

class HomeActivity : HubBaseActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        //todo: setup user information from here

    }
}