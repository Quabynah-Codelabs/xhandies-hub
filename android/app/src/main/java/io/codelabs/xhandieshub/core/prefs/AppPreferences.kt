package io.codelabs.xhandieshub.core.prefs

import android.content.Context
import androidx.core.content.edit
import io.codelabs.xhandieshub.core.Utils

/**
 * Stores user details in a persistent storage on the device
 */
class AppPreferences private constructor(context: Context) {

    private val prefs = context.getSharedPreferences(Utils.APP_PREFS, Context.MODE_PRIVATE)
    val isLoggedIn: Boolean
        get() = !uid.isNullOrEmpty()

    // Stores user's unique id
    var uid: String? = null
        get() = prefs.getString("uid", null)
        set(value) {
            prefs.edit {
                putString("uid", value)
                apply()
            }
            field = value
        }

    companion object {
        @Volatile
        private var instance: AppPreferences? = null

        /**
         * Creates a singleton instance of the [AppPreferences]
         */
        fun getInstance(context: Context): AppPreferences = instance ?: synchronized(this) {
            instance ?: AppPreferences(context).also { instance = it }
        }
    }
}