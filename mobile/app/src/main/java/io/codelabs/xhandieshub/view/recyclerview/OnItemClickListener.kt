package io.codelabs.xhandieshub.view.recyclerview

interface OnItemClickListener<Item> {

    fun onClick(item: Item, isLong: Boolean = false)
}