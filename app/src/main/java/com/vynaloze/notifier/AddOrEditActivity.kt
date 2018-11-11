package com.vynaloze.notifier

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_or_edit.*

class AddOrEditActivity : AppCompatActivity() {
    private val TAG = "AddOrEdit"
    private val MAX_PERIODS: Int = 3
    private var oldReminder: Reminder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edit)
        preparePeriodListView()
        loadOldReminder()
    }

    fun save(view: View) {
        val reminder = parse() ?: return
        deleteIfPresent()
        ReminderDao.insert(reminder)
        ReminderScheduler.scheduleNext(reminder)
        Log.i(TAG, "Saved $reminder")
        Log.i(TAG, "Current state: ${ReminderDao.getAll()}")
        finish()
    }

    fun delete(view: View) {
        deleteIfPresent()
        Log.i(TAG, "Current state: ${ReminderDao.getAll()}")
        finish()
    }

    private fun deleteIfPresent() {
        if (oldReminder != null) {
            Log.i(TAG, "Deleted $oldReminder")
            ReminderDao.delete(oldReminder!!)
        }
    }

    private fun parse(): Reminder? {
        val title = edit_title.text.toString()
        if (title.isBlank()) {
            Toast.makeText(this, "Title must not be null", Toast.LENGTH_SHORT).show()
            return null
        }
        val desc = edit_desc.text.toString()
        val periods = mutableListOf<Int>()
        for (i in 0 until MAX_PERIODS) {
            val e = edit_periods.getChildAt(i) as EditText
            periods.add(e.text.toString().toIntOrNull() ?: break)
        }
        if (periods.size == 0) {
            Toast.makeText(this, "Please add at least one period", Toast.LENGTH_SHORT).show()
            return null
        }
        return Reminder(title, desc, periods)
    }

    private fun preparePeriodListView() {
        for (i in 0 until MAX_PERIODS) {
            val e = EditText(this)
            e.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            e.inputType = 2
            edit_periods.addView(e)
        }
    }

    private fun loadOldReminder() {
        oldReminder = intent.getSerializableExtra(Reminder::class.simpleName) as? Reminder
                ?: return
        edit_title.setText(oldReminder!!.title)
        for (i in 0 until MAX_PERIODS) {
            if (i >= oldReminder!!.periods.size) return
            val e = edit_periods.getChildAt(i) as EditText
            e.setText(oldReminder!!.periods[i].toString())
        }
    }
}
