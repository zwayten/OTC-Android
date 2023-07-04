package com.example.orangetrainingcenterandroid.FCM

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.orangetrainingcenterandroid.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {
    private val CHANNEL_ID = "your_channel_id" // Replace with your own channel ID
    private val channelName = "Your Channel Name" // Replace with your own channel name
    private val channelDescription = "Your Channel Description" // Replace with your own channel description

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Your Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)

        // Display the notification
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Log the received data
        Log.d(TAG, "Received FCM message")

        // Display the notification
        showNotification(remoteMessage)
    }
}

/*
class FCMService : FirebaseMessagingService(){


     private val CHANNEL_ID = "your_channel_id" // Replace with your own channel ID
     private val channelName = "Your Channel Name" // Replace with your own channel name
     private val channelDescription = "Your Channel Description" // Replace with your own channel description
     private var NOTIFICATION_ID = 0
    private lateinit var notificationManager: NotificationManager

     override fun onCreate() {
         super.onCreate()

         createNotificationChannel()
         notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
     }

     private fun createNotificationChannel() {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val channel = NotificationChannel(
                 CHANNEL_ID,
                 "Your Channel Name",
                 NotificationManager.IMPORTANCE_DEFAULT
             )
             notificationManager = getSystemService(NotificationManager::class.java)
             notificationManager.createNotificationChannel(channel)
         }
     }

     private fun showNotification(title: String?, message: String?) {
         // Generate a unique notification ID
         NOTIFICATION_ID = System.currentTimeMillis().toInt()

         // Build the notification
         val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
             .setContentTitle(title)
             .setContentText(message)
             .setSmallIcon(R.drawable.ic_notification)
             .setAutoCancel(true)

         // Display the notification
         notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

     }

     override fun onMessageReceived(remoteMessage: RemoteMessage) {
         // Log the received data
         Log.d(TAG, "Received FCM message: ${remoteMessage.data}")

         // Process the data and display the notification
         if (remoteMessage.data.isNotEmpty()) {
             val title = remoteMessage.data["title"]
             val message = remoteMessage.data["message"]
             showNotification(title, message)

         }
     }

    override fun onNewToken(token: String) {
        // Handle token refresh here

    }


}

 */