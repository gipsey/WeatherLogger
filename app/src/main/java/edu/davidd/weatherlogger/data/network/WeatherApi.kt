package edu.davidd.weatherlogger.data.network

import edu.davidd.weatherlogger.data.network.model.WeatherDto

interface WeatherApi {

    suspend fun getWeather(lat: Double, lon: Double): WeatherDto
}