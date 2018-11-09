package com.vynaloze.notifier

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    //TODOs:
    //always running in the background
    //persist all the reminders somewhere in memory
    //initialise list of reminders
    //add / edit reminder activity test

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val adapter = ReminderAdapter(this)
        reminder_list.adapter = adapter

        reminder_add.setOnClickListener { view ->
            val intent = Intent(this, AddOrEditActivity::class.java)
            intent.putExtra(Reminder::class.simpleName, false)
            startActivity(intent)
        }
    }

}
