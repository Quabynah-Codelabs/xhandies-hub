package io.codelabs.xhandieshub.core.datasource

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import io.codelabs.xhandieshub.core.util.Constants


/**
 * Shared preferences implementation for logged in users
 */
class Preferences private constructor(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)
    var isLoggedIn: Boolean = false
    var uid: String? = null
        get() {
            return prefs.getString(Constants.Prefs.UID, null)
        }
        set(value) {
            if (field == value) return
            field = value
            isLoggedIn = !uid.isNullOrEmpty()
            prefs.edit {
                putString(Constants.Prefs.UID, value)
                apply()
            }
        }

    companion object {
        @Volatile
        private var instance: Preferences? = null

        fun get(context: Context): Preferences {
            return instance ?: synchronized(this) {
                instance ?: Preferences(context).also { instance = it }
            }
        }
    }
}