package io.codelabs.xhandieshub.view

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import io.codelabs.sdk.util.toast
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.databinding.ActivityFoodBinding
import io.codelabs.xhandieshub.model.Food
import io.codelabs.xhandieshub.viewmodel.FoodViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FoodDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityFoodBinding
    private val foodViewModel by viewModel<FoodViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_food)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        if (intent.hasExtra(FOOD)) {
            val food = intent.getParcelableExtra<Food>(FOOD)
            binding.food = food
            bindUI()
        } else {
            toast("Cannot view details of this food")
            finishAfterTransition()
        }
    }

    private fun bindUI() {
        // todo: bind UI
    }

    companion object {
        const val FOOD = "food"
    }

    fun addToCart(view: View) {
        toast("Added to cart")
        foodViewModel.addToCart(binding.food as Food)
        finishAfterTransition()
    }
}