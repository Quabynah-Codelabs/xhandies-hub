package io.codelabs.xhandieshub.view

import android.os.Bundle
import android.view.View
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity

/**
 * Authentication Screen
 * Users sign up or login to their accounts
 */
class PasswordRecoverActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)
    }

    fun recoverPassword(view: View) {

    }

}