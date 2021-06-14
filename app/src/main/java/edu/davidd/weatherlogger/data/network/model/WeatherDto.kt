package edu.davidd.weatherlogger.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
    val name: String,
    val main: WeatherMainDto,
    val coord: WeatherCoordinatedDto,
)

@Serializable
data class WeatherMainDto(
    val temp: Double
)

@Serializable
data class WeatherCoordinatedDto(
    val lon: Double,
    val lat: Double,
)