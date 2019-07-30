package io.codelabs.xhandieshub.core

import android.util.Patterns
import io.codelabs.sdk.util.debugLog

/**
 * For debugging
 */
fun Any?.debugger(msg: Any?) = debugLog("Xhandies Hub: ${msg.toString()}")


/**
 * Checks if an email field matches the pattern required for authentication or not
 */
fun String.matchesEmailPattern(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Lambda Callback function
 */
typealias Callback<R> = (R) -> Unit