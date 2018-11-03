package com.vynaloze.notifier

import android.util.Log
import java.util.*

class Reminder(val title: String, vararg periods: Int) {
    val periodList: List<Long> = periods.map { i -> i * 60 * 1000L }
    private var nextPeriodIndex: Int = 0

    var active: Boolean = true
        set(value) {
            field = value
            if (active) scheduleNext()
            else timer.cancel()
        }

    private val timer: Timer = Timer()

    init {
        scheduleNext()
    }

    private fun scheduleNext() {
        if (active) {
            timer.schedule(ReminderAction(), periodList[nextPeriodIndex])
            nextPeriodIndex = if (nextPeriodIndex + 1 < periodList.size) nextPeriodIndex + 1 else 0
        }
    }

    inner class ReminderAction : TimerTask() {
        override fun run() {
            Log.i("Reminder", "Notification!")
            Log.i("Reminder", "Next one in " + periodList[nextPeriodIndex])
            pushNotification(title)
            scheduleNext()
        }
    }
}