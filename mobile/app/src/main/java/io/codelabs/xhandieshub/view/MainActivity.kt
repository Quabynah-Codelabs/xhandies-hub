package io.codelabs.xhandieshub.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.intentTo
import io.codelabs.sdk.util.showConfirmationToast
import io.codelabs.sdk.util.toast
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.HubBaseActivity
import io.codelabs.xhandieshub.data.User
import io.codelabs.xhandieshub.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : HubBaseActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val RC_SIGN_IN = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    }

    fun performLogin(v: View?) {
        if (prefs.isLoggedIn) {
            intentTo(HomeActivity::class.java, true)
        } else {
            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
            toggleButton()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                debugLog("Google sign in failed.\n" + e.localizedMessage)
                toggleButton(true)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null && !prefs.isLoggedIn) {
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account != null) firebaseAuthWithGoogle(account)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        if (account == null) {
            debugLog("Unable to login with this account")
            toggleButton(true)
            return
        }

        debugLog("Login token: ${account.idToken}")
        toast("Signing in to your account...")

        // Login with google account an intent user to the next activity
        auth.signInWithCredential(GoogleAuthProvider.getCredential(account.idToken, null)).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                ioScope.launch {
                    // Get user's information from the login
                    val firebaseUser = it.result?.user

                    // Store user's key locally in shared preferences
                    prefs.uid = firebaseUser?.uid
                    // Create new user instance in  the room database
                    dao.addUser(
                        User(
                            firebaseUser?.uid!!,
                            firebaseUser.displayName,
                            firebaseUser.email,
                            firebaseUser.photoUrl.toString()
                        )
                    )

                    uiScope.launch {
                        // Show user login confirmation toast
                        showConfirmationToast(
                            firebaseUser.photoUrl.toString(),
                            firebaseUser.displayName ?: firebaseUser.email ?: "New User"
                        )

                        // Send user to the home screen
                        intentTo(HomeActivity::class.java, true)
                    }
                }
            } else {
                toast(it.exception?.localizedMessage, true)
                toggleButton(true)
            }
        }.addOnFailureListener(this) {
            toast(it.localizedMessage, true)
            toggleButton(true)
        }
    }

    private fun toggleButton(state: Boolean = false) {
        binding.loginButton.isEnabled = state
    }
}
