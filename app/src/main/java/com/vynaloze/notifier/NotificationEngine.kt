package com.vynaloze.notifier

import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import java.util.*

object NotificationEngine {

    //schedule reminder as observer


    private const val TIMEOUT: Long = 1000


    fun pushNotification(reminder: Reminder) {
        val mBuilder = NotificationCompat.Builder(ApplicationContextProvider.getContext(), "IGNORED")
                .setSmallIcon(R.drawable.navigation_empty_icon) //todo
                .setContentTitle(reminder.title)
                .setContentText(reminder.desc)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(ApplicationContextProvider.getContext())) {
            notify(reminder.id.hashCode(), mBuilder.build())
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                val notifManager = ApplicationContextProvider.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notifManager.cancel(reminder.id.hashCode())
            }
        }, TIMEOUT)
    }
}