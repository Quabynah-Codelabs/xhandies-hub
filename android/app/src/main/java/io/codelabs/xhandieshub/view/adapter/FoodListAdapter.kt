package io.codelabs.xhandieshub.view.adapter

import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.core.intentTo
import io.codelabs.xhandieshub.model.Food
import io.codelabs.xhandieshub.view.FoodDetailsActivity

class FoodListAdapter(private val context: BaseActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int =
        if (datasource.isEmpty()) R.layout.item_empty_food else R.layout.item_food

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_food -> FoodViewHolder(
                DataBindingUtil.inflate(
                    context.layoutInflater,
                    viewType,
                    parent,
                    false
                )
            )

            R.layout.item_empty_food -> EmptyViewHolder(
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
        if (getItemViewType(position) == R.layout.item_food) {
            if (holder is FoodViewHolder) {
                val food = datasource[position]
                holder.bind(food)
                holder.itemView.setOnClickListener {
                    context.intentTo(
                        FoodDetailsActivity::class.java, bundleOf(
                            Pair(FoodDetailsActivity.FOOD, food)
                        )
                    )
                }
            }
        }
    }

    fun addFoods(foods: MutableList<Food?>) {
        if (foods.isEmpty()) return
        foods.forEach { dish ->
            val add = !datasource.contains(dish)

            if (add) {
                datasource.add(dish)
                notifyItemRangeChanged(0, foods.size)
            }
        }
    }

    fun isEmpty(): Boolean = datasource.isEmpty()

    private val datasource = mutableListOf<Food?>()


}