package io.codelabs.xhandieshub.core.base

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import io.codelabs.sdk.view.BaseActivity
import io.codelabs.xhandieshub.core.prefs.AppPreferences
import org.koin.android.ext.android.inject

/**
 * Base class for all activities
 * Contains global variables obtained from dependency injection
 */
abstract class BaseActivity : BaseActivity() {

    /**
     * Firebase SDKs
     */
    val auth by inject<FirebaseAuth>()
    val db by inject<FirebaseFirestore>()
    val bucket by inject<StorageReference>()

    /**
     * Shared Preferences
     */
    val prefs by inject<AppPreferences>()

}