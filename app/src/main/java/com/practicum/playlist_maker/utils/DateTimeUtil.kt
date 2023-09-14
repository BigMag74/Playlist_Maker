package com.practicum.playlist_maker.utils

import com.practicum.playlist_maker.R
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

    fun trackWordEndingId(countOfTracks: Int): Int {
        return if (countOfTracks in 10..20) {
            R.string.tracks
        } else {
            when (countOfTracks % 10) {
                1 -> R.string.tracks_1
                2, 3, 4 -> R.string.tracks_2
                else -> R.string.tracks
            }
        }
    }

    fun minuteWordEndingId(minutes: Int): Int {
        return if (minutes in 10..20) {
            R.string.minutes
        } else {
            when (minutes % 10) {
                1 -> R.string.minutes_1
                2, 3, 4 -> R.string.minutes_2
                else -> R.string.minutes
            }
        }
    }

    object Format {
        const val YYYY_MM_DD = "yy-MM-dd"
        const val MM_SS = "mm:ss"
    }
}