package io.codelabs.xhandieshub.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.tasks.Tasks
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.codelabs.sdk.util.intentTo
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.Utils
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.core.debugger
import io.codelabs.xhandieshub.model.User
import io.codelabs.xhandieshub.view.adapter.FoodListAdapter
import io.codelabs.xhandieshub.view.adapter.HomePagerAdapter
import io.codelabs.xhandieshub.viewmodel.FoodViewModel
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
    private val foodViewModel by viewModel<FoodViewModel>()
    private lateinit var foodAdapter: FoodListAdapter
    private var hasCartItems: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Set support for toolbar actions
        setSupportActionBar(toolbar)

        // Setup view pager
        setupPager()

        // Setup recyclerview
        setupListOfFoods()

        // get live feedback about user's information
        userViewModel.currentUser.observe(this, Observer { user ->
            debugger("Logged in user: $user")
            if (user == null) {
                ioScope.launch {
                    if (prefs.isLoggedIn) {
                        try {
                            val currentUser =
                                Tasks.await(db.collection(Utils.USER_COLLECTION).document(prefs.uid!!).get())
                                    .toObject(User::class.java)

                            // Update the local user's information
                            userViewModel.updateUser(currentUser)
                        } catch (e: Exception) {
                            debugger(e.localizedMessage)
                        }
                    } else debugger("You are not logged in properly")
                }
            }
        })
    }

    private fun setupListOfFoods() {
        foodAdapter = FoodListAdapter(this)
        food_list.adapter = foodAdapter

        food_list.setHasFixedSize(false)
        food_list.itemAnimator = DefaultItemAnimator()
        food_list.layoutManager =  GridLayoutManager(this, 2).apply {
            this.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (foodAdapter.isEmpty()) 2 else 1
                }
            }
        }

        // Kick-off initial load
        foodViewModel.getAllLocalFoods().observe(this@HomeActivity, Observer { foods ->
            debugger("All foods from database: ${foods?.size}")
            if (foods != null) foodAdapter.addFoods(foods)
        })

        // Cart
        foodViewModel.cart.observe(this@HomeActivity, Observer { carts ->
            hasCartItems = carts?.isNotEmpty() ?: false
            invalidateOptionsMenu()
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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.item_track)?.isVisible = hasCartItems
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cart -> // Navigate to user's shopping cart
                intentTo(CartActivity::class.java)
            R.id.item_track -> // Navigate to item tracking
                intentTo(TrackingActivity::class.java)
            R.id.item_logout -> MaterialAlertDialogBuilder(this@HomeActivity).apply {
                setTitle("Confirm logout")
                setMessage("Do you wish to sign out?")
                setPositiveButton("Yes") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    signOut()
                }
                setNegativeButton("No") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                show()
            }
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