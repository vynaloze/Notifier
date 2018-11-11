package com.vynaloze.notifier

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.PersistableBundle
import java.util.*

object ReminderScheduler {
    private val scheduler: JobScheduler = ApplicationContextProvider.getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

    fun scheduleNext(reminder: Reminder) {
        val id = reminder.id
        val period = reminder.getPeriodAndIncrement()
        val job = buildJob(id, period)
        scheduler.schedule(job)
    }

    private fun buildJob(id: UUID, period: Long): JobInfo {
        val bundle = PersistableBundle()
        bundle.putString("id", id.toString())
        return JobInfo.Builder(id.hashCode(), ComponentName(ApplicationContextProvider.getContext(), ReminderJobService::class.java))
                .setMinimumLatency(period)
                .setOverrideDeadline((period * 1.05).toLong())
                .setExtras(bundle)
                .build()
    }
}