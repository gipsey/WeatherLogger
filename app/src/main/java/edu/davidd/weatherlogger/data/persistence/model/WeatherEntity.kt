package edu.davidd.weatherlogger.data.persistence.model

data class WeatherEntity(
    val date: Long,
    val tempInKelvin: Double,
    val location: String,
    val lat: Double,
    val lon: Double
)