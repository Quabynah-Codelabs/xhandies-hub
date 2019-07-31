package io.codelabs.xhandieshub.view.adapter

import androidx.recyclerview.widget.RecyclerView
import io.codelabs.xhandieshub.databinding.ItemCartBinding
import io.codelabs.xhandieshub.model.Cart
import io.codelabs.xhandieshub.model.Food

/**
 * Cart view holder for showing each item in a user's shopping cart
 */
class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(cart: Cart, food: Food) {
        binding.cart = cart
        binding.food = food
    }
}