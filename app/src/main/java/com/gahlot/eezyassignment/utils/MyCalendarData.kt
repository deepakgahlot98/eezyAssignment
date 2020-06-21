package com.gahlot.eezyassignment.utils

import java.text.SimpleDateFormat
import java.util.*


class myCalendarData(start: Int) {
    var day = 0
        private set
    var month = 0
        private set
    var year = 0
        private set
    private var dayofweek = 0
    var weekDay: String? = null
        private set
    private val dateFormat = SimpleDateFormat("E")
    private val calendar: Calendar = Calendar.getInstance()

    private fun setThis() {
        day = calendar[Calendar.DAY_OF_MONTH]
        month = calendar[Calendar.MONTH]
        year = calendar[Calendar.YEAR]
        dayofweek = calendar[Calendar.DAY_OF_WEEK]
        weekDay = dateFormat.format(calendar.time)
    }

    fun getNextWeekDay(nxt: Int) {
        calendar.add(Calendar.DATE, nxt)
        setThis()
    }

    // constructor
    init {
        calendar.add(Calendar.DATE, start)
        setThis()
    }
}