package edu.davidd.weatherlogger.data.network

import edu.davidd.weatherlogger.data.network.model.WeatherDto
import io.ktor.client.*
import io.ktor.client.request.*

class WeatherApiOkhttp(private val client: HttpClient) : WeatherApi {

    override suspend fun getWeather(lat: Double, lon: Double) =
        client.get<WeatherDto> {
            parameter("lat", lat)
            parameter("lon", lon)
        }
}