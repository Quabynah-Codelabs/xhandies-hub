package io.codelabs.xhandieshub.core

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import io.codelabs.sdk.util.debugLog
import io.codelabs.xhandieshub.core.datasource.Preferences
import io.codelabs.xhandieshub.core.injection.firebaseModule
import io.codelabs.xhandieshub.core.injection.roomModule
import io.codelabs.xhandieshub.core.injection.sharedPrefsModule
import io.codelabs.xhandieshub.core.util.Constants
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * [Application] subclass
 */
class HubApplication : Application() {

    private val prefs: Preferences /*= Preferences.get(applicationContext)*/ by inject()
    private val firestore: FirebaseFirestore /*= FirebaseAuth.getInstance()*/ by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@HubApplication)

            modules(firebaseModule, sharedPrefsModule, roomModule)
        }

        if (prefs.isLoggedIn) {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result?.token

                    // Customers database reference
                    firestore.collection(Constants.Database.CUSTOMERS).document(prefs.uid!!)
                        .set(
                            mapOf<String, Any?>(
                                "token" to token,
                                "updatedAt" to System.currentTimeMillis()
                            )
                            , SetOptions.merge()
                        )
                        .addOnCompleteListener { task1 ->
                            if (task1.isSuccessful) {
                                debugLog("Token updated successfully")
                            } else {
                                debugLog(task1.exception?.localizedMessage)
                            }
                        }.addOnFailureListener {
                            debugLog(it.localizedMessage)
                        }
                }
            }
        }
    }
}