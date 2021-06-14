package edu.davidd.weatherlogger.data

import androidx.preference.PreferenceManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import edu.davidd.weatherlogger.BuildConfig
import edu.davidd.weatherlogger.data.network.WeatherApi
import edu.davidd.weatherlogger.data.network.WeatherApiOkhttp
import edu.davidd.weatherlogger.data.persistence.WeatherDao
import edu.davidd.weatherlogger.data.persistence.WeatherDaoPreferences
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import org.koin.dsl.module
import timber.log.Timber

val dataDiModule = module {

    factory {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
    factory { PreferenceManager.getDefaultSharedPreferences(get()) }
    factory<WeatherDao> { WeatherDaoPreferences(get(), get()) }

    factory {
        HttpClient(OkHttp) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = false
                        prettyPrint = true
                    }
                )
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.v(message)
                    }
                }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 10_000L
                connectTimeoutMillis = 10_000L
                socketTimeoutMillis = 10_000L
            }

            defaultRequest {
                url("https://api.openweathermap.org/data/2.5/weather")
                parameter("APPID", BuildConfig.API_APP_ID)
            }
        }
    }
    factory<WeatherApi> { WeatherApiOkhttp(get()) }
}