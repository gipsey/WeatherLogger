package edu.davidd.weatherlogger.framework.util

import android.content.Context
import edu.davidd.weatherlogger.R

class WeatherTemperatureFormatter {

    fun format(temperatureInCelsius: Double, context: Context): String =
        context.getString(
            R.string.temperature_values,
            temperatureInCelsius
        )
}