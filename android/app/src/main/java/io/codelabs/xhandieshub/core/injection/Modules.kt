package io.codelabs.xhandieshub.core.injection

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import io.codelabs.xhandieshub.BuildConfig
import io.codelabs.xhandieshub.core.prefs.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Inject Firebase module
 */
val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseMessaging.getInstance() }
    single {
        FirebaseStorage.getInstance().reference.child(
            BuildConfig.APPLICATION_ID.replace(
                ".",
                ""
            )
        )
    }
}

/**
 * Inject Room module
 */
val roomModule = module {
    single { }
}

/**
 * Inject prefs module
 */
val prefsModule = module {
    single { AppPreferences.getInstance(androidContext()) }
}