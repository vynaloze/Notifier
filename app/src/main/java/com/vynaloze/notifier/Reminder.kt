package com.vynaloze.notifier

import android.util.Log
import java.io.Serializable
import java.util.*

class Reminder(val title: String, val periods: List<Int>) : Serializable {
    val periodsInMilis: List<Long> = periods.map { i -> i * 1000L } //fixme!!!  * 60
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
            timer.schedule(ReminderAction(), periodsInMilis[nextPeriodIndex])
            nextPeriodIndex = if (nextPeriodIndex + 1 < periodsInMilis.size) nextPeriodIndex + 1 else 0
        }
    }

    override fun toString(): String {
        return "Reminder(title='$title', periods=$periods, periodsInMilis=$periodsInMilis, nextPeriodIndex=$nextPeriodIndex, active=$active)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Reminder) return false

        if (title != other.title) return false
        if (periods != other.periods) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + periods.hashCode()
        return result
    }

    inner class ReminderAction : TimerTask() {
        override fun run() {
            Log.i("Reminder", "Notification!")
            Log.i("Reminder", "Next one in " + periodsInMilis[nextPeriodIndex])
            pushNotification(title)
            scheduleNext()
        }
    }
}