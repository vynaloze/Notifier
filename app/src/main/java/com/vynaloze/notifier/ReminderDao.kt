package com.vynaloze.notifier

import android.util.Log
import java.io.File
import java.io.IOException
import java.util.*

object ReminderDao {
    private val filename = "reminders.csv"
    private val cache = mutableListOf<Reminder>()
    private val TAG = "ReminderDao"

    init {
        readFromFile()
    }

    private fun readFromFile() {
        try {
            val file = File(ApplicationContextProvider.getContext().filesDir, filename)
            file.readLines().map { it.toReminder() }.forEach { cache += it }
        } catch (e: IOException) {
            Log.w(TAG, e.toString())
        }
    }

    //todo delete?
    @Deprecated("todo delete")
    fun get(title: String, periods: List<Int>): Reminder? =
            ReminderDao.getAll().firstOrNull { it.title == title && it.periods == periods }

    fun getAll(): List<Reminder> {
        return cache
    }

    //todo delete?
    @Deprecated("todo delete")
    fun upsert(reminder: Reminder) {
        val i = cache.indexOfFirst { it.id == reminder.id } //FIXME 2. Does not work as intended - old one is in UI, no trace of new (except of notifications)
        if (i != -1) {
            cache.removeAt(i)   //todo use delete(reminder) instead
        }
        cache.add(reminder)

        try {
            upsertToFile(reminder)
        } catch (e: IOException) {
            Log.w(TAG, e.toString())
            File(ApplicationContextProvider.getContext().filesDir, filename).printWriter().use { it.write("") }
            upsertToFile(reminder)
        }
    }

    fun insert(reminder: Reminder) {
        cache.add(reminder)
        try {
            upsertToFile(reminder) //fixme
        } catch (e: IOException) {
            Log.w(TAG, e.toString())
            File(ApplicationContextProvider.getContext().filesDir, filename).printWriter().use { it.write("") }
            upsertToFile(reminder)
        }
    }

    private fun upsertToFile(reminder: Reminder) {
        val file = File(ApplicationContextProvider.getContext().filesDir, filename)
        var lines: List<String> = listOf()
        var updated = false
        file.readLines().forEach {
            if (it.getReminderId() == reminder.id) {
                lines += reminder.toCsv()
                updated = true
            } else {
                lines += it
            }
        }
        if (!updated) {
            lines += reminder.toCsv()
        }
        file.printWriter().use { out ->
            lines.forEach {
                out.println(it)
            }
        }
    }

    fun delete(reminder: Reminder) {
        val i = cache.indexOfFirst { it.id == reminder.id }
        if (i != -1) {
            cache.removeAt(i)
        }


        val file = File(ApplicationContextProvider.getContext().filesDir, filename) //todo handle if someone would click delete while 0 reminders saved
        var lines: List<String> = listOf()
        file.readLines().forEach {
            if (it.getReminderId() != reminder.id) {
                lines += it
            }
        }
        file.printWriter().use { out ->
            lines.forEach {
                out.println(it)
            }
        }
    }

    private fun Reminder.toCsv(): String {
        return "${this.id},${this.title},${this.active},${this.periods.joinToString(",")}"
    }

    private fun String.toReminder(): Reminder {
        val splitted = this.split(',') //TODO switch to another delimiter
        val id = UUID.fromString(splitted[0])
        val title = splitted[1]
        val active = splitted[2].toBoolean()
        val periods = splitted.drop(3).map { it.toInt() }
        return Reminder(id, title, periods, active)
    }

    private fun String.getReminderId(): UUID {
        return UUID.fromString(this.split(',')[0]) //TODO switch to another delimiter
    }
}