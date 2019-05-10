package io.codelabs.xhandieshub.core.injection

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.codelabs.xhandieshub.core.datasource.Preferences
import io.codelabs.xhandieshub.core.datasource.room.HubRoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Firebase SDK initialization
 */
val firebaseModule = module {

    single { FirebaseApp.initializeApp(androidContext()) }

    single { FirebaseAuth.getInstance(get()) }

    single { FirebaseFirestore.getInstance(get()) }

    single { FirebaseStorage.getInstance().reference }
}

val sharedPrefsModule = module {
    single { Preferences.get(androidContext()) }
}

val roomModule = module {

    single { HubRoomDatabase.getInstance(androidContext()).dao() }
}