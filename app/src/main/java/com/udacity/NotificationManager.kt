package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.MainActivity.Companion.CHANNEL_ID
import com.udacity.MainActivity.Companion.NOTIFICATION_ID

fun NotificationManager.sendNotification(
    messageBody: String,
    applicationContext: Context,
    fileName: String,
    status: String
) {
    val detailIntent = Intent(applicationContext, DetailActivity::class.java)
    detailIntent.putExtra("Status",status)
    detailIntent.putExtra("FileName",fileName)
    val buttonPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        detailIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val builder = NotificationCompat.Builder(
        applicationContext,
        CHANNEL_ID
    ).setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            buttonPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}