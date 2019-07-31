package io.codelabs.xhandieshub.view.adapter

import androidx.recyclerview.widget.RecyclerView
import io.codelabs.xhandieshub.databinding.ItemFoodBinding
import io.codelabs.xhandieshub.model.Food

/**
 * Food List ViewHolder
 */
class FoodViewHolder(private val binding: ItemFoodBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(food: Food) {
        binding.food = food
    }

}