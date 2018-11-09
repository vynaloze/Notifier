package com.vynaloze.notifier

import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import java.util.*


fun pushNotification(title: String) {
    val mBuilder = NotificationCompat.Builder(ApplicationContextProvider.getContext(), "IGNORED")
            .setSmallIcon(R.drawable.navigation_empty_icon) //todo
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setTimeoutAfter(1000)
//            .setVibrate()

    val uniqueId = (Date().time / 1000L % Integer.MAX_VALUE).toInt()
    with(NotificationManagerCompat.from(ApplicationContextProvider.getContext())) {
        notify(uniqueId, mBuilder.build())
    }

    Timer().schedule(object : TimerTask() {
        override fun run() {
            val notifManager = ApplicationContextProvider.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifManager.cancel(uniqueId)
        }
    }, 1000)
}