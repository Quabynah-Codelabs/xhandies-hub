package io.codelabs.xhandieshub.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.android.material.snackbar.Snackbar
import io.codelabs.recyclerview.GridItemDividerDecoration
import io.codelabs.recyclerview.SlideInItemAnimator
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.intentTo
import io.codelabs.sdk.util.toast
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.HubBaseActivity
import io.codelabs.xhandieshub.core.util.Constants
import io.codelabs.xhandieshub.data.Product
import io.codelabs.xhandieshub.databinding.ActivityHomeBinding
import io.codelabs.xhandieshub.view.recyclerview.OnItemClickListener
import io.codelabs.xhandieshub.view.recyclerview.ProductsAdapter
import kotlinx.coroutines.launch

class HomeActivity : HubBaseActivity(), OnItemClickListener<Product> {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        setSupportActionBar(binding.bottomBar)

        // Setup RecyclerView
        binding.productsGrid.layoutManager = GridLayoutManager(this, 1) as RecyclerView.LayoutManager?
        binding.productsGrid.setHasFixedSize(true)
        binding.productsGrid.itemAnimator = SlideInItemAnimator()
        binding.productsGrid.addItemDecoration(GridItemDividerDecoration(this, R.dimen.divider_height, R.color.divider))
        binding.productsGrid.adapter = ProductsAdapter(this, this, firestore, prefs).apply {
            ioScope.launch {
                try {
                    val products = fetchProducts()

                    uiScope.launch {
                        addProducts(products)
                    }
                } catch (e: Exception) {
                    debugLog(e.localizedMessage)
                }
            }
        }
    }

    private fun fetchProducts(): MutableList<Product> =
        Tasks.await(firestore.collection(Constants.Database.PRODUCTS).get()).toObjects(Product::class.java)

    fun showCart(v: View?) = intentTo(CartActivity::class.java)

    override fun onClick(item: Product, isLong: Boolean) {
        if (isLong) {
            return
        }

        toast(item)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        if (menu is MenuBuilder) menu.setOptionalIconsVisible(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_logout -> {
                Snackbar.make(binding.container, "Confirm logout?", Snackbar.LENGTH_LONG).apply {
                    setAction(getString(R.string.logout)) {
                        dao.getCurrentUser(prefs.uid!!).observe(this@HomeActivity, Observer {
                            if (it != null) {
                                ioScope.launch {
                                    dao.deleteUser(it)
                                    prefs.uid = null

                                    uiScope.launch {
                                        intentTo(MainActivity::class.java, true)
                                    }
                                }
                            } else toast("you are not logged in properly")
                        })
                    }
                    show()
                }
            }
            R.id.menu_orders -> {
                //todo
            }
            R.id.menu_recents -> {
                //todo
            }
            R.id.menu_search -> {
                //todo
            }
        }
        return super.onOptionsItemSelected(item)
    }

}