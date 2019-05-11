package io.codelabs.xhandieshub.core.util

import io.codelabs.xhandieshub.R
import kotlin.random.Random

/**
 * Application Constants
 */
object Constants {
    const val SHARED_PREFS = "HUB_SHARED_PREFS"
    const val ROOM_DATABASE = "ROOM_DATABASE"

    /**
     * Database references
     */
    object Database {
        const val CUSTOMERS = "customers"
        const val CART = "customers/%s/cart"
        const val PRODUCTS = "products"
    }

    /**
     * Notification constants
     */
    object Notification {
        const val TOKEN_UPDATE = "new-user-token-update"
        const val CHANNEL_ID = "HUB_CHANNEL_ID"
        const val NOTIFICATION_ICON = R.drawable.shr_logo
        val NOTIFICATION_ID = Random(3).nextInt()
        const val RC_NOTIFICATION = 3
    }

    /**
     * Shared preference constants
     */
    object Prefs {
        const val UID = "UID"
    }
}