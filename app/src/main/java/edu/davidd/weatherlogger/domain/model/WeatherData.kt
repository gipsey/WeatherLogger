package edu.davidd.weatherlogger.domain.model

data class WeatherData(
    val date: Long,
    val temperatureInCelsius: Double,
    val location: String
)