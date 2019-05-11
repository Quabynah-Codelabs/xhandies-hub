package io.codelabs.xhandieshub.core.datasource.remote

import io.codelabs.xhandieshub.data.Product
import kotlin.random.Random

object FakeDataSource {

    fun getFakeProducts(): MutableList<Product> {
        val products: MutableList<Product> = mutableListOf()

        for (i in 0 until 100) {
            val product = Product(
                "$i",
                "Samosa",
                "${Random.nextInt(10, 1999)}",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum",
                "https://firebasestorage.googleapis.com/v0/b/xhandieshub.appspot.com/o/Aloo-Samosa-1.jpg?alt=media&token=cbedd531-f2af-46cf-859c-f6f9f1dc7e4a"
            )
            products.add(product)
        }
        return products
    }

}