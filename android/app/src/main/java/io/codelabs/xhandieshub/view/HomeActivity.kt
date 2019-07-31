package io.codelabs.xhandieshub.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.Tasks
import io.codelabs.sdk.util.intentTo
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.Utils
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.core.debugger
import io.codelabs.xhandieshub.model.User
import io.codelabs.xhandieshub.view.adapter.HomePagerAdapter
import io.codelabs.xhandieshub.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch
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

        // Set support for toolbar actions
        setSupportActionBar(toolbar)

        // Setup view pager
        setupPager()

        // get live feedback about user's information
        userViewModel.currentUser.observe(this, Observer { user ->
            debugger("Logged in user: $user")
            if (user == null) {
                ioScope.launch {
                    if (prefs.isLoggedIn) {
                        val currentUser =
                            Tasks.await(db.collection(Utils.USER_COLLECTION).document(prefs.uid!!).get())
                                .toObject(User::class.java)

                        // Update the local user's information
                        userViewModel.updateUser(currentUser)
                    } else debugger("You are not logged in properly")
                }
            }
        })
    }

    private fun setupPager() {
        val adapter = HomePagerAdapter(this)
        pager_items.adapter = adapter
        indicator.setViewPager(pager_items)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_cart) {
            // Navigate to user's shopping cart
            intentTo(CartActivity::class.java)
        }
        return super.onOptionsItemSelected(item)
    }

    // Sign out any currently logged in user
    private fun signOut() {
        auth.signOut()
        prefs.signOut()
        intentTo(AuthActivity::class.java, true)
    }

}