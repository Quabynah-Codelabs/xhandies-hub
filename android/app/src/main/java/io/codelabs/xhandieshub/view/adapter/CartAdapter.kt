package io.codelabs.xhandieshub.view.adapter

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.core.debugger
import io.codelabs.xhandieshub.model.Cart
import io.codelabs.xhandieshub.model.Food
import io.codelabs.xhandieshub.viewmodel.FoodViewModel

class CartAdapter(private val context: BaseActivity, private val viewModel: FoodViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val datasource = mutableListOf<Cart>()

    override fun getItemViewType(position: Int): Int =
        if (datasource.isEmpty()) R.layout.item_empty_cart else R.layout.item_cart

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_cart -> CartViewHolder(
                DataBindingUtil.inflate(
                    context.layoutInflater,
                    viewType,
                    parent,
                    false
                )
            )

            R.layout.item_empty_cart -> EmptyViewHolder(
                context.layoutInflater.inflate(
                    viewType,
                    parent,
                    false
                )
            )

            else -> throw IllegalStateException("VH not found")
        }
    }

    override fun getItemCount(): Int = if (datasource.isEmpty()) 1 else datasource.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == R.layout.item_cart) {
            if (holder is CartViewHolder) {
                val cart = datasource[position]
                viewModel.getFoodByKey(cart.foodId).observe(context, Observer { food ->
                    holder.bind(cart, food)
                    holder.itemView.setOnClickListener { debugger("${cart.foodId} clicked") }
                })
            }
        }
    }

    fun addCarts(carts: MutableList<Cart>) {
        this.datasource.clear()
        this.datasource.addAll(carts)
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean = datasource.isEmpty()


}