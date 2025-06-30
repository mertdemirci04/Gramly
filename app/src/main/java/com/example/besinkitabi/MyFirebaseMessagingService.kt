package com.example.besinkitabi

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.recyclerviewlist.R
import com.example.recyclerviewlist.R.drawable.diet
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            sendNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
        } else if (remoteMessage.data.isNotEmpty()) {
            // Veri mesajları için ek işleme yapabilirsiniz
            val title = remoteMessage.data["title"]
            val messageBody = remoteMessage.data["body"]
            sendNotification(title, messageBody)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Token'ı loglayın veya sunucuya gönderin
        Log.d("FCM Token", token)
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val channelId = "default_channel_id"
        val channelName = "Default Channel"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Bildirime tıklandığında açılacak intent
        val intent = Intent(this, MainActivity::class.java)  // Bildirim tıklandığında MainActivity açılır.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // PendingIntent güncellenmesi
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE  // FLAG_IMMUTABLE eklendi
        )

        // Android 8.0 ve üstü için bildirim kanalı oluştur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        // Bildirimi oluştur
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.diet)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)  // Bildirim tıklandığında bu intent çalışacak
            .setAutoCancel(true)  // Bildirim tıklandıktan sonra kaybolur

        // Bildirimi göster
        notificationManager.notify(0, notificationBuilder.build())
    }


}
