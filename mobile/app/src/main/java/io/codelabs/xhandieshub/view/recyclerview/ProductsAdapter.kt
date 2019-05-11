package io.codelabs.xhandieshub.view.recyclerview

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.codelabs.sdk.glide.GlideApp
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.data.Product
import kotlinx.android.synthetic.main.item_product.view.*

class ProductsAdapter constructor(
    private val context: Activity,
    private val listener: OnItemClickListener<Product>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val inflater: LayoutInflater = context.layoutInflater
    private val products: MutableList<Product> = mutableListOf()

    companion object {
        private val TYPE_EMPTY = R.layout.item_empty
        private val TYPE_PRODUCT = R.layout.item_product
    }

    override fun getItemViewType(position: Int): Int {
        return if (products.isEmpty()) TYPE_EMPTY else TYPE_PRODUCT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY -> EmptyViewHolder(inflater.inflate(TYPE_EMPTY, parent, false))
            TYPE_PRODUCT -> ProductViewHolder(inflater.inflate(TYPE_PRODUCT, parent, false))
            else -> throw IllegalArgumentException("Please pass a valid viewholder")
        }
    }

    override fun getItemCount(): Int {
        return if (products.isEmpty()) 1 else products.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_PRODUCT) {
            bindProductsViewHolder(holder as ProductViewHolder, position)
        }
    }

    private fun bindProductsViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.v.setOnClickListener {
            listener.onClick(product)
        }

        holder.v.setOnLongClickListener {
            listener.onClick(product, true)
            true
        }

        holder.v.product_name.text = product.name
        holder.v.add_product.setOnClickListener {
            listener.onClick(product)
        }
        holder.v.product_desc.text = product.description
        holder.v.product_price.text = String.format("GHC %s", product.price)
        GlideApp.with(context)
            .load(product.image)
            .placeholder(R.color.content_placeholder)
            .error(R.color.content_placeholder)
            .into(holder.v.product_image)

    }

    fun addProducts(products: MutableList<Product>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }
}