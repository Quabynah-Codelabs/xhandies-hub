package io.codelabs.xhandieshub.core

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import io.codelabs.sdk.util.debugLog
import io.codelabs.xhandieshub.core.base.BaseActivity

/**
 * For debugging
 */
fun Any?.debugger(msg: Any?) = debugLog("Xhandies Hub: ${msg.toString()}")

/**
 * Get the [LayoutInflater] from [Context]
 */
val Context.layoutInflater: LayoutInflater get() = LayoutInflater.from(this)


val Context.locationManager: LocationManager get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager

/**
 * Checks if an email field matches the pattern required for authentication or not
 */
fun String.matchesEmailPattern(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun BaseActivity.intentTo(target: Class<out BaseActivity>, bundle: Bundle) {
    startActivity(Intent(this, target).apply {
        putExtras(bundle)
    })
}

/**
 * Lambda Callback function
 */
typealias Callback<R> = (R) -> Unit