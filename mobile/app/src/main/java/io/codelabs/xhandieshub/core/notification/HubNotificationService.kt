package io.codelabs.xhandieshub.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.codelabs.sdk.util.debugLog
import io.codelabs.xhandieshub.R
import io.codelabs.xhandieshub.core.datasource.Preferences
import io.codelabs.xhandieshub.core.util.Constants
import io.codelabs.xhandieshub.core.util.Constants.Notification.CHANNEL_ID
import io.codelabs.xhandieshub.core.util.Constants.Notification.NOTIFICATION_ICON
import io.codelabs.xhandieshub.core.util.Constants.Notification.NOTIFICATION_ID
import io.codelabs.xhandieshub.core.util.Constants.Notification.RC_NOTIFICATION
import io.codelabs.xhandieshub.view.HomeActivity
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
            createNotificationChannel()

            val notificationData = p0.data
            debugLog(notificationData)

            // Send push notification
            pushNotification(
                notificationData["title"], notificationData["message"], PendingIntent.getActivity(
                    applicationContext,
                    RC_NOTIFICATION,
                    Intent(applicationContext, HomeActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        }
    }

    private fun pushNotification(textTitle: String?, textContent: String?, pi: PendingIntent? = null) {
        //Get notification service
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        //Create notification builder
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(NOTIFICATION_ICON)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setVibrate(longArrayOf(0, 200, 0, 200))
            .setAutoCancel(true)
            /*.setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, imageUrl))
            )*/
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (pi != null) builder.setContentIntent(pi)

        if (manager != null) {
            //Send notification
            manager.notify(NOTIFICATION_ID, builder.build())

            //Wake the screen when the device is locked
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            val wakeLock = powerManager.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.PARTIAL_WAKE_LOCK,
                HubNotificationService::class.java.canonicalName
            )
            wakeLock.acquire(15000)
        }
    }

    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    private fun createNotificationChannel() {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            val name = CHANNEL_ID
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}