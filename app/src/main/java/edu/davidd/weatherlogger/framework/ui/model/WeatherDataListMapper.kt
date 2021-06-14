package edu.davidd.weatherlogger.framework.ui.model

import android.content.Context
import androidx.core.os.ConfigurationCompat
import edu.davidd.weatherlogger.domain.model.WeatherData
import edu.davidd.weatherlogger.framework.util.WeatherDateFormatter
import edu.davidd.weatherlogger.framework.util.WeatherTemperatureFormatter
import java.util.*

class WeatherDataListMapper(
    private val dateFormatter: WeatherDateFormatter,
    private val temperatureFormatter: WeatherTemperatureFormatter,
) {

    operator fun invoke(context: Context, list: List<WeatherData>) =
        list.map {
            it.run {
                val locale = ConfigurationCompat.getLocales(context.resources.configuration).get(0) ?: Locale.getDefault()

                WeatherItem(
                    dateFormatter.format(date, locale),
                    temperatureFormatter.format(temperatureInCelsius, context),
                    location
                )
            }
        }
}