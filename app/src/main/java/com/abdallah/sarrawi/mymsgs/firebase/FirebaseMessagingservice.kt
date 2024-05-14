package com.abdallah.sarrawi.mymsgs.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.abdallah.sarrawi.mymsgs.R
import com.abdallah.sarrawi.mymsgs.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class FirebaseMessagingservice : FirebaseMessagingService() {

    var notifiaction: NotificationCompat.Builder? = null
    var bitmap: Bitmap? = null
    var id = "Default"


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val title = remoteMessage.notification!!.title
        val body = remoteMessage.notification!!.body
        val imgurl = remoteMessage.data["image"]
        val tag = remoteMessage.data["tag"]
        bitmap = getbitmap(imgurl)

        getnotifiacation(bitmap?.let { it }, title?.let { it }, body?.let { it }, imgurl?.let { it })

    }

    override fun onNewToken(token: String) {
        // قم بتنفيذ العمليات المطلوبة هنا

    }


    private fun getnotifiacation(bitmap: Bitmap?, title: String?, body: String?, imgurl: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder = NotificationCompat.Builder(this, id)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.s)

        // تحقق من قيمة bitmap و imgurl قبل استخدامها
        if (bitmap != null && imgurl != null) {
            notificationBuilder.setLargeIcon(getbitmap(imgurl))
            notificationBuilder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                id, "notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }



    fun getbitmap(imgurl: String?): Bitmap? {
        return try {
            val url = URL(imgurl)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}