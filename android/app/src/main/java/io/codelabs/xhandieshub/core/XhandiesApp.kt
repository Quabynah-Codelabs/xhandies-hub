package io.codelabs.xhandieshub.core

import android.app.Application
import com.google.firebase.FirebaseApp
import io.codelabs.xhandieshub.BuildConfig
import io.codelabs.xhandieshub.core.injection.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class XhandiesApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.getInstance().apply {
            debugger("Firebase initialized as: $name")
        }

        // Initialize Dependency Injection
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.INFO)
            androidContext(this@XhandiesApp)
            modules(arrayListOf(locationModule,firebaseModule, roomModule, prefsModule, paymentModule))
        }
    }

}