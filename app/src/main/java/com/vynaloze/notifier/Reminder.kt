package com.vynaloze.notifier

import android.util.Log
import java.io.Serializable
import java.util.*

class Reminder(val id: UUID, val title: String, val periods: List<Int>, active: Boolean) : Serializable {
    constructor(title: String, periods: List<Int>) : this(UUID.randomUUID(), title, periods, true)

    private val TAG = "Reminder"
    val periodsInMilis: List<Long> = periods.map { i -> i * 1000L } //fixme!!!  * 60
    private var nextPeriodIndex: Int = 0

    var active: Boolean = active
        set(value) {
            timer?.cancel()
            timer?.purge()
            timer = Timer()
            field = value
            if (active) {
                Log.i(TAG, "Will schedule in set function.")
                scheduleNext()
            }
        }

    @Transient
    private var timer: Timer? = Timer() //FIXME 1. Decouple timer from pojo

    init {
        Log.i(TAG, "Will schedule in init block.")
        scheduleNext()
    }

    private fun scheduleNext() {
        Log.i(TAG, "Scheduled.")
        if (active) {
            timer?.schedule(ReminderAction(), periodsInMilis[nextPeriodIndex])
            nextPeriodIndex = if (nextPeriodIndex + 1 < periodsInMilis.size) nextPeriodIndex + 1 else 0
        }
    }


    override fun toString(): String {
        return "Reminder(title='$title', periods=$periods, id=$id, periodsInMilis=$periodsInMilis, nextPeriodIndex=$nextPeriodIndex, active=$active)"
    }

    inner class ReminderAction : TimerTask() {
        override fun run() {
            Log.i(TAG, "Notification!")
            Log.i(TAG, "Next one in " + periodsInMilis[nextPeriodIndex])
            pushNotification(title)
            scheduleNext()
        }
    }
}