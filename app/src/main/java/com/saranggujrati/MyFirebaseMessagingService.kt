package com.saranggujrati


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.saranggujrati.ui.activity.MainActivity
import com.saranggujrati.ui.activity.StartMainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "Push Notification"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //Log.d("From: ", remoteMessage.from.toString())
        if (remoteMessage.notification != null) {
            //showNotification(remoteMessage.notification!!.title,remoteMessage.notification!!.body)
            showNotification(remoteMessage.data["bodyText"], remoteMessage.data["organization"], remoteMessage.notification!!.clickAction.toString())
            //Log.e("From: " , remoteMessage.from.toString())
            //This will give you the Text property in the curl request(Sample Message):
            //Log.e("Notification Message Body: " , remoteMessage.notification!!.body.toString())
            //This is where you get your click_action
            //Log.e("Notification Click Action: " , remoteMessage.notification!!.clickAction.toString())
            //put code here to navigate based on click_action
        }
    }

    private fun showNotification(title: String?, message: String?, click_action: String) {
        // Pass the intent to switch to the MainActivity
        val intent : Intent
        if (click_action.equals("STARTMAINACTIVITY")) {
            intent = Intent(this, StartMainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        } else {
            intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT)

        //val intent = Intent(this, MainActivity::class.java)
        // Assign channel ID
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Pass the intent to PendingIntent to start the
        // next Activity
        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,channelId)
            .setSmallIcon(R.drawable.ic_logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setBadgeIconType(R.drawable.ic_logo)

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(getCustomDesign(title!!, message!!))
        } // If Android Version is lower than Jelly Beans,
        else {
            builder = builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_logo)
        }
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }

    // Method to get the custom Design for the display of
    // notification.
    private fun getCustomDesign(title: String,message: String): RemoteViews {
        val remoteViews = RemoteViews("com.saranggujrati",R.layout.notification)
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        remoteViews.setImageViewResource(R.id.icon,R.drawable.ic_logo)
        return remoteViews
    }


}




