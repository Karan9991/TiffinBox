package com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFireBaseMessagingService : FirebaseMessagingService() {
    var title: String? = null
    var message: String? = null
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        title = remoteMessage.data["Title"]
        message = remoteMessage.data["Message"]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("wwwwwwwwwwwwwwwwwwwwwww", "wow")
            val channel = NotificationChannel("General", "General notification", NotificationManager.IMPORTANCE_DEFAULT)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            val attributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, attributes)
            val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.createNotificationChannel(channel)
        }
        //        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.bg_circle)
//                        .setContentTitle(title)
//                        .setContentText(message);
//        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0, builder.build());
    }
}