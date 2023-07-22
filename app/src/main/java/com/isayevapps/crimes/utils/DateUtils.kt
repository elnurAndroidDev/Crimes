package com.isayevapps.crimes.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateUtils {
    companion object {
        fun getDateString(date: Date): String {
            return SimpleDateFormat("dd.MM.yyyy").format(date)
        }
        fun getTimeString(date: Date): String {
            return SimpleDateFormat("HH:mm").format(date)
        }
        fun getDateAndTimeString(date: Date): String {
            return SimpleDateFormat("dd.MM.yyyy, HH:mm").format(date)
        }
    }
}