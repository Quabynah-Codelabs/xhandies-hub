package io.codelabs.xhandieshub.view

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import io.codelabs.sdk.util.intentTo
import io.codelabs.sdk.util.toast
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.core.matchesEmailPattern
import io.codelabs.xhandieshub.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Authentication Screen
 * Users login to their accounts
 */
class AuthActivity : BaseActivity() {

    private val viewModel by viewModel<UserViewModel>()

    private lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // Setup snackbar
        snackbar = Snackbar.make(container, "Please wait...", Snackbar.LENGTH_INDEFINITE)

        email.addTextChangedListener { text ->
            login_button.isEnabled =
                text != null && text.toString().matchesEmailPattern() && password.text != null
                        && password.text.toString().length >= 6
        }

        password.addTextChangedListener { text ->
            login_button.isEnabled =
                text != null && text.toString().length >= 6 && email.text.toString().matchesEmailPattern()
        }
    }

    fun resetPassword(view: View) {
        intentTo(PasswordRecoverActivity::class.java)
    }

    fun loginUser(view: View) {
        toggleLoading(true, email, password, view, reset_button)

        // Login user and get the Firebase User instance
        viewModel.loginUser(email.text.toString(), password.text.toString()) { user ->
            if (user == null) {
                uiScope.launch {
                    toggleLoading(false, email, password, view, reset_button)
                    toast("User could be logged in for some reason")
                }
            } else intentTo(HomeActivity::class.java, true)
        }
    }


    private fun toggleLoading(isShown: Boolean, vararg toggleViews: View) {
        if (isShown)
            snackbar.show()
        else snackbar.dismiss()

        for (view in toggleViews) {
            view.isEnabled = !isShown
        }
    }

    fun signUp(view: View) {
        intentTo(SignUpActivity::class.java, true)
    }

}