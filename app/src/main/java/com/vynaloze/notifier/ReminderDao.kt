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

    fun getAll(): List<Reminder> {
        return cache
    }

    fun insert(reminder: Reminder) {
        cache.add(reminder)
        appendToFile(reminder)
    }

    private fun appendToFile(reminder: Reminder) {
        val file = File(ApplicationContextProvider.getContext().filesDir, filename)
        file.printWriter().use { out ->
            file.readLines().forEach {
                out.println(it)
            }
            out.println(reminder.toCsv())
        }
    }

    fun delete(reminder: Reminder) {
        cache.remove(reminder)
        deleteFromFile(reminder)
    }

    private fun deleteFromFile(reminder: Reminder) {
        val file: File
        try {
            file = File(ApplicationContextProvider.getContext().filesDir, filename)
        } catch (e: IOException) {
            Log.w(TAG, "Nothing to delete.")
            return
        }
        var lines: List<String> = listOf()
        file.readLines().forEach {
            if (it.toReminder() != reminder) {
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
        return "${this.id}|${this.title}|${this.desc}|${this.active}|${this.periods.joinToString("|")}"
    }

    private fun String.toReminder(): Reminder {
        val splitted = this.split('|')
        val id = UUID.fromString(splitted[0])
        val title = splitted[1]
        val desc = splitted[2]
        val active = splitted[3].toBoolean()
        val periods = splitted.drop(4).map { it.toInt() }
        return Reminder(id, title, desc, periods, active)
    }
}