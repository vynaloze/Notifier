package com.vynaloze.notifier

import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import java.util.*

class ReminderJobService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        val uuid = UUID.fromString(params?.extras?.get("id").toString())
        val reminder = ReminderDao.getById(uuid) ?: return false
        if (reminder.active) {
            pushNotification(reminder)
            ReminderScheduler.scheduleNext(reminder)
        }
        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    private val TIMEOUT: Long = 5000 //todo click -> go to app. and change to 10s? 20?

    private fun pushNotification(reminder: Reminder) {
        val mBuilder = NotificationCompat.Builder(ApplicationContextProvider.getContext(), "IGNORED")
                .setSmallIcon(R.drawable.navigation_empty_icon) //todo
                .setContentTitle(reminder.title)
                .setContentText(reminder.desc)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(ApplicationContextProvider.getContext())) {
            notify(reminder.id.hashCode(), mBuilder.build())
        }

        //todo rethink this TimerTask approach. And maybe this method should be elsewhere?
        Timer().schedule(object : TimerTask() {
            override fun run() {
                val notifManager = ApplicationContextProvider.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notifManager.cancel(reminder.id.hashCode())
            }
        }, TIMEOUT)
    }
}
