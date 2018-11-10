package com.vynaloze.notifier

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    //TODO : always running in the background
    private var adapter: ReminderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        adapter = ReminderAdapter(this)
        reminder_list.adapter = adapter

        reminder_add.setOnClickListener { view ->
            val intent = Intent(this, AddOrEditActivity::class.java)
            intent.putExtra(Reminder::class.simpleName, false)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
    }
}
