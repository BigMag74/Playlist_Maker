package com.practicum.playlist_maker.utils

import com.practicum.playlist_maker.utils.DateTimeUtil.Format.MM_SS
import com.practicum.playlist_maker.utils.DateTimeUtil.Format.YYYY_MM_DD
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

object DateTimeUtil {

    fun msecToMMSS(msec: Int): String {
        return SimpleDateFormat(MM_SS, Locale.getDefault()).format(msec)
    }

    fun stringToYear(string: String): String {
        val calendar: Calendar = GregorianCalendar()
        calendar.time =
            SimpleDateFormat(YYYY_MM_DD, Locale.getDefault()).parse(string) as Date
        return calendar.get(Calendar.YEAR).toString()
    }

    object Format {
        const val YYYY_MM_DD = "yy-MM-dd"
        const val MM_SS = "mm:ss"
    }
}