package io.codelabs.xhandieshub.view

import android.os.Bundle
import androidx.lifecycle.Observer
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.core.debugger
import io.codelabs.xhandieshub.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Home screen
 * User can browse through items here & add them to his/her shopping cart
 */
class HomeActivity : BaseActivity() {

    private val userViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // get live feedback about user's information
        userViewModel.currentUser.observe(this, Observer { user ->
            debugger("Logged in user: $user")
        })
    }
}