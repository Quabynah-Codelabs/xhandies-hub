package io.codelabs.xhandieshub.view

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import io.codelabs.sdk.util.toast
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.core.matchesEmailPattern
import io.codelabs.xhandieshub.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_recover_password.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Authentication Screen
 * Users sign up or login to their accounts
 */
class PasswordRecoverActivity : BaseActivity() {
    private lateinit var snackbar: Snackbar
    private val userViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)
        // Setup snackbar
        snackbar = Snackbar.make(container, "Please wait...", Snackbar.LENGTH_INDEFINITE)

        email.addTextChangedListener { text ->
            reset_button.isEnabled =
                text != null && text.toString().matchesEmailPattern()
        }
    }

    fun recoverPassword(view: View) {
        userViewModel.resetPassword(email.text.toString()) {
            uiScope.launch {
                toast("Email to reset password has been sent successfully", true)
                finish()
            }
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

}