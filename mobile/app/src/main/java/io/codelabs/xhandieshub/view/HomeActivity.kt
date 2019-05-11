package io.codelabs.xhandieshub.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuBuilder
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import io.codelabs.recyclerview.GridItemDividerDecoration
import io.codelabs.recyclerview.SlideInItemAnimator
import io.codelabs.sdk.util.intentTo
import io.codelabs.sdk.util.toast
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.HubBaseActivity
import io.codelabs.xhandieshub.core.datasource.remote.FakeDataSource
import io.codelabs.xhandieshub.core.util.Constants
import io.codelabs.xhandieshub.data.Product
import io.codelabs.xhandieshub.databinding.ActivityHomeBinding
import io.codelabs.xhandieshub.view.recyclerview.OnItemClickListener
import io.codelabs.xhandieshub.view.recyclerview.ProductsAdapter

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
        binding.productsGrid.adapter = ProductsAdapter(this, this).apply {
            //            debugLog(fetchProducts())
//            addProducts(fetchProducts())
            addProducts(FakeDataSource.getFakeProducts())
        }
    }

    private fun fetchProducts(): MutableList<Product> =
        Tasks.await(firestore.collection(Constants.Database.PRODUCTS).get()).toObjects(Product::class.java)

    fun showCart(v: View?) = intentTo(CartActivity::class.java)

    override fun onClick(item: Product, isLong: Boolean) {
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

        }
        return super.onOptionsItemSelected(item)
    }

}