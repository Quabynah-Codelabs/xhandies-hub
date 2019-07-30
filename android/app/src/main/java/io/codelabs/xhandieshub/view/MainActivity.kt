package io.codelabs.xhandieshub.view

import android.os.Bundle
import android.view.View
import io.codelabs.sdk.util.intentTo
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    // Click on get started
    fun getStarted(view: View) {
        if (prefs.isLoggedIn)
            intentTo(HomeActivity::class.java, true)
        else
            intentTo(AuthActivity::class.java, true)
    }
}
