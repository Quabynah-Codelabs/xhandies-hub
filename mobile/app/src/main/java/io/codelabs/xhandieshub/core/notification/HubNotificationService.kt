package io.codelabs.xhandieshub.core.notification

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.codelabs.sdk.util.debugLog
import io.codelabs.xhandieshub.core.datasource.Preferences
import io.codelabs.xhandieshub.core.util.Constants
import org.koin.android.ext.android.inject

/**
 * Notification service
 */
class HubNotificationService : FirebaseMessagingService() {
    private val prefs: Preferences /*= Preferences.get(applicationContext)*/ by inject()
    private val firestore: FirebaseFirestore /*= FirebaseAuth.getInstance()*/ by inject()

    override
    fun onNewToken(p0: String?) {
        if (prefs.isLoggedIn) {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result?.token

                    // Customers database reference
                    firestore.collection(Constants.Database.CUSTOMERS).document(prefs.uid!!)
                        .set(
                            mapOf<String, Any?>(
                                "token" to token
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

    override fun onMessageReceived(p0: RemoteMessage?) {
        if (p0 != null) {
            val notificationData = p0.data
            debugLog(notificationData)

            //todo: setup notification for the application
        }
    }
}