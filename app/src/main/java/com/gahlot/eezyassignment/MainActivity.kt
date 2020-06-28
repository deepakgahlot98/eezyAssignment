package com.gahlot.eezyassignment

import CalendarAdapter
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private val lastDayInCalendar = Calendar.getInstance(Locale.ENGLISH)
    private val sdf = SimpleDateFormat("EEE d, MMM yy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)

    // current date
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]

    // selected date
    private var selectedDay: Int = currentDay
    private var selectedMonth: Int = currentMonth
    private var selectedYear: Int = currentYear

    private var day: String? = null

    // all days in month
    private val dates = ArrayList<Date>()

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        actionBar?.title = "My Plan"

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(calendar_recycler_view)

        lastDayInCalendar.add(Calendar.MONTH, 6)

        setUpCalendar()

        evening_card_default.setOnClickListener{
            if (evening_card_detail.visibility  == View.GONE) {
                TransitionManager.beginDelayedTransition(evening_card, AutoTransition())
                evening_card_detail.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    evening_card_default.setBackgroundColor(getColor(R.color.colorAccent))
                }
                ticketView.visibility = View.VISIBLE
            } else {
                TransitionManager.beginDelayedTransition(evening_card, AutoTransition())
                evening_card_detail.visibility = View.GONE
                ticketView.visibility = View.GONE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    evening_card_default.setBackgroundColor(getColor(R.color.white))
                }
            }
        }

    }

    private fun setUpCalendar(changeMonth: Calendar? = null) {

        txt_selected_date!!.text = sdf.format(currentDate.time)

        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_YEAR)

        selectedDay =
            when {
                changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
                else -> currentDay
            }
        selectedMonth =
            when {
                changeMonth != null -> changeMonth[Calendar.MONTH]
                else -> currentMonth
            }
        selectedYear =
            when {
                changeMonth != null -> changeMonth[Calendar.YEAR]
                else -> currentYear
            }

        var currentPosition = 0
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        while (dates.size < maxDaysInMonth) {
            if (monthCalendar[Calendar.DAY_OF_MONTH] == selectedDay)
                currentPosition = currentDay
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        // Assigning calendar view.
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        calendar_recycler_view!!.layoutManager = layoutManager
        val calendarAdapter = CalendarAdapter(this, dates, currentDate, changeMonth)
        calendar_recycler_view!!.adapter = calendarAdapter

        /**
         * If you start the application, it centers the current day, but only if the current day
         * is not one of the first (1, 2, 3) or one of the last (29, 30, 31).
         */
        when {
            currentPosition > 2 -> calendar_recycler_view!!.scrollToPosition(currentPosition - 3)
            maxDaysInMonth - currentPosition < 2 -> calendar_recycler_view!!.scrollToPosition(currentPosition)
            else -> calendar_recycler_view!!.scrollToPosition(currentPosition)
        }


        /**
         * After calling up the OnClickListener, the text of the current month and year is changed.
         * Then change the selected day.
         */
        calendarAdapter.setOnItemClickListener(object : CalendarAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickCalendar = Calendar.getInstance()
                clickCalendar.time = dates[position]
                txt_selected_date!!.text = sdf.format(clickCalendar.time)
                selectedDay = clickCalendar[Calendar.DAY_OF_WEEK]
                val sdf1 = SimpleDateFormat("EEE MMM d HH:mm:ss", Locale.ENGLISH)
                sdf1.applyPattern("E")
                val dayInWeek = sdf1.parse(clickCalendar.time.toString())!!
                day = sdf1.format(dayInWeek).toString().subSequence(0,1) as String
            }
        })

        calendar_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var firstVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
                var v: CalendarAdapter.ViewHolder = calendar_recycler_view.findViewHolderForAdapterPosition(firstVisiblePosition) as CalendarAdapter.ViewHolder
                if (v?.txtDayInWeek.text.equals(day)) {
                    v?.txtDay.setBackgroundResource(R.drawable.circle_selected)
                } else {
                    v?.txtDay.setBackgroundResource(R.drawable.circle_drawable)
                }
            }
        })
    }
}

