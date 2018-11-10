package com.vynaloze.notifier

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.reminder_info.view.*

class ReminderAdapter(val context: Context) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.reminder_info, parent, false)

        val reminder = getItem(position) as Reminder
        rowView.reminder_title.text = reminder.title
        rowView.reminder_details.text = "${reminder.desc} - ${reminder.periods}"
        rowView.reminder_switch.isChecked = reminder.active
        rowView.reminder_switch.setOnCheckedChangeListener { _, isChecked ->
            reminder.active = isChecked
            Log.i("Switched reminder", reminder.toString())
        }
        rowView.reminder_edit.setOnClickListener({ _ ->
            val intent = Intent(context, AddOrEditActivity::class.java)
            intent.putExtra(Reminder::class.simpleName, reminder)
            context.startActivity(intent)
        })

        return rowView
    }

    override fun getCount(): Int {
        return ReminderDao.getAll().size
    }

    override fun getItem(position: Int): Any {
        return ReminderDao.getAll()[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}