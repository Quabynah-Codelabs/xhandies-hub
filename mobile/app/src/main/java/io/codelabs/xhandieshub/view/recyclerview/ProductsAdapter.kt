package io.codelabs.xhandieshub.view.recyclerview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import io.codelabs.sdk.glide.GlideApp
import io.codelabs.sdk.util.debugLog
import io.codelabs.sdk.util.toast
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.HubBaseActivity
import io.codelabs.xhandieshub.core.datasource.Preferences
import io.codelabs.xhandieshub.core.util.Constants
import io.codelabs.xhandieshub.data.Cart
import io.codelabs.xhandieshub.data.Product
import io.codelabs.xhandieshub.view.ProductActivity
import kotlinx.android.synthetic.main.item_product.view.*

class ProductsAdapter constructor(
    private val context: HubBaseActivity,
    private val listener: OnItemClickListener<Product>,
    private val firestore: FirebaseFirestore,
    private val prefs: Preferences
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
        var cartKey = ""
        holder.v.setOnClickListener {
            Intent(context, ProductActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelable(ProductActivity.EXTRA_PRODUCT, product)
                })
                context.startActivity(this)
            }
        }

        holder.v.setOnLongClickListener {
            listener.onClick(product, true)
            true
        }

        holder.v.product_name.text = product.name
        holder.v.add_product.setOnClickListener { view ->
            if ((view as MaterialButton).icon == context.getDrawable(R.drawable.twotone_remove_shopping_cart_24px) && cartKey.isNotEmpty()) {
                view.text = context.getString(R.string.add_to_cart)
                view.icon = context.getDrawable(R.drawable.twotone_add_shopping_cart_24px)

                val document = firestore.collection(String.format(Constants.Database.CART, prefs.uid)).document(cartKey)
                document.delete()
                    .addOnCompleteListener {
                        debugLog("Removing item from cart: ${it.isSuccessful}")
                    }.addOnFailureListener { ex ->
                        debugLog(ex.localizedMessage)
                        view.text = context.getString(R.string.added_to_cart)
                        view.icon = context.getDrawable(R.drawable.twotone_remove_shopping_cart_24px)
                        context.toast("Failed to remove \"${product.name}\" from your shopping cart", true)
                    }
            } else {
                view.text = context.getString(R.string.added_to_cart)
                view.icon = context.getDrawable(R.drawable.twotone_remove_shopping_cart_24px)
                val document = firestore.collection(String.format(Constants.Database.CART, prefs.uid)).document()
                cartKey = document.id
                document.set(Cart(document.id, product.key))
                    .addOnCompleteListener {
                        debugLog("Adding item to cart: ${it.isSuccessful}")
                    }.addOnFailureListener { ex ->
                        debugLog(ex.localizedMessage)
                        view.text = context.getString(R.string.add_to_cart)
                        view.icon = context.getDrawable(R.drawable.twotone_add_shopping_cart_24px)
                        context.toast("Failed to add \"${product.name}\" to your shopping cart", true)
                    }
            }
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