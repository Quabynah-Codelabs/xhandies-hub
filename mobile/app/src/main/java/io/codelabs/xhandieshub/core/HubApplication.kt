package io.codelabs.xhandieshub.core

import android.app.Application
import io.codelabs.xhandieshub.core.injection.firebaseModule
import io.codelabs.xhandieshub.core.injection.roomModule
import io.codelabs.xhandieshub.core.injection.sharedPrefsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * [Application] subclass
 */
class HubApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@HubApplication)

            modules(firebaseModule, sharedPrefsModule, roomModule)
        }
    }
}