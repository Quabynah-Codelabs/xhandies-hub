package io.codelabs.xhandieshub.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.EditText
import io.codelabs.sdk.util.debugLog
import io.codelabs.xhandieshub.core.base.BaseActivity
import io.codelabs.xhandieshub.model.Food

/**
 * For debugging
 */
fun Any?.debugger(msg: Any?) = debugLog("Xhandies Hub: ${msg.toString()}")

/**
 * Get the [LayoutInflater] from [Context]
 */
val Context.layoutInflater: LayoutInflater get() = LayoutInflater.from(this)

/**
 * Checks if an email field matches the pattern required for authentication or not
 */
fun String.matchesEmailPattern(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Creates a simple intent for an [BaseActivity] subclass
 */
fun BaseActivity.intentTo(target: Class<out BaseActivity>, bundle: Bundle) {
    startActivity(Intent(this, target).apply {
        putExtras(bundle)
    })
}

val EditText.hasValue: Boolean get() = this.text.toString().isNotEmpty()

val EditText.value: String get() = this.text.toString()

fun MutableList<Food>.addIfDoesNotExist(otherList: MutableList<Food>) {
    if (otherList.isEmpty()) return
    otherList.forEach { item ->
        debugger("ID: ${item.key}")
        var add = true
        for (food in this) {
            add = food.key != item.key
        }
        if (add) this.add(item)
    }
}

fun MutableList<Food?>.clearAndAdd(otherList: MutableList<Food?>) {
    this.clear()
    this.addAll(otherList)
}

/**
 * Lambda Callback function
 */
typealias Callback<R> = (R) -> Unit