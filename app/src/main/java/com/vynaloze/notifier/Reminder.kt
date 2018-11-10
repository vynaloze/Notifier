package com.vynaloze.notifier

import java.io.Serializable
import java.util.*

class Reminder(
        val id: UUID,
        val title: String,
        val desc: String,
        val periods: List<Int>,
        var active: Boolean
) : Serializable {

    constructor(title: String, desc: String, periods: List<Int>) :
            this(UUID.randomUUID(), title, desc, periods, true)

    val periodsInMilis: List<Long> = periods.map { i -> i * 1000L } //todo  * 60
    var nextPeriodIndex: Int = 0

    private val TAG = "Reminder"

    override fun toString(): String {
        return "Reminder(id=$id, title='$title', desc='$desc', periods=$periods, active=$active, periodsInMilis=$periodsInMilis, nextPeriodIndex=$nextPeriodIndex)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Reminder) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}