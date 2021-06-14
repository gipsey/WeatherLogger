package edu.davidd.weatherlogger.framework.util

import java.text.DateFormat
import java.util.*

class WeatherDateFormatter {

    fun format(millis: Long, locale: Locale): String =
        DateFormat
            .getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, locale)
            .format(Date(millis))
}