package com.gahlot.eezyassignment.model

import java.text.SimpleDateFormat
import java.util.*

class MyCalendar {


    // storing day - like Sun, Wed etc..,
    // date like 1, 2 etc..
    //Month 1..12
    // year .. 2019.. beyond
    private var day: String? = null
    var date: String? = null
    var month: String? = null
    var year: String? = null
    var pos = 0

    constructor() {}
    constructor(
        day: String?,
        date: String?,
        month: String,
        year: String?,
        i: Int
    ) {
        this.day = day
        this.date = date
        this.month = getMonthStr(month)
        this.year = year
        pos = i
    }

    private fun getMonthStr(month: String): String {
        val cal = Calendar.getInstance()
        val month_date = SimpleDateFormat("MMM")
        val monthnum = month.toInt()
        cal[Calendar.MONTH] = monthnum
        return month_date.format(cal.time)
    }

    fun getDay(): String? {
        return day
    }

    fun setDay(date: String?) {
        day = day
    }
}