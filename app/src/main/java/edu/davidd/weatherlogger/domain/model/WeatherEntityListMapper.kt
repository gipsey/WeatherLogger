package edu.davidd.weatherlogger.domain.model

import edu.davidd.weatherlogger.data.persistence.model.WeatherEntity

class WeatherEntityListMapper {

    operator fun invoke(list: List<WeatherEntity>) =
        list.map {
            it.run {
                WeatherData(
                    date,
                    convertKelvinToCelsius(tempInKelvin),
                    location,
                )
            }
        }

    private fun convertKelvinToCelsius(tempInKelvin: Double) =
        tempInKelvin - 273.15
}