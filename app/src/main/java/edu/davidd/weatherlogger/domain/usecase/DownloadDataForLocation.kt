package edu.davidd.weatherlogger.domain.usecase

import edu.davidd.weatherlogger.data.network.WeatherApi
import edu.davidd.weatherlogger.data.persistence.WeatherDao
import edu.davidd.weatherlogger.domain.CurrentTimeProvider
import edu.davidd.weatherlogger.domain.model.WeatherDtoMapper
import timber.log.Timber

class DownloadDataForLocation(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao,
    private val mapper: WeatherDtoMapper,
    private val currentTimeProvider: CurrentTimeProvider,
) : UseCase<DownloadDataForLocation.Params, Unit>() {

    override suspend fun run(params: Params): Either<String, Unit> =
        try {
            val currentTime = currentTimeProvider.get()
            val downloadedDto = weatherApi.getWeather(params.lat, params.lon)
            val downloadedEntity = mapper(currentTime, downloadedDto)

            val entities = listOf(
                *weatherDao.get().filterNot { it.location == downloadedEntity.location }.toTypedArray(),
                downloadedEntity
            )
            weatherDao.save(entities)

            Either.Right(Unit)
        } catch (e: Exception) {
            Timber.w(e, "DownloadDataForLocation failure")
            Either.Left(e.localizedMessage ?: "unknown error")
        }

    data class Params(val lat: Double, val lon: Double)
}