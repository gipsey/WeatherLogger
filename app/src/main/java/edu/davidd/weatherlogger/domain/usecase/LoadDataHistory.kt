package edu.davidd.weatherlogger.domain.usecase

import edu.davidd.weatherlogger.data.persistence.WeatherDao
import edu.davidd.weatherlogger.domain.model.WeatherData
import edu.davidd.weatherlogger.domain.model.WeatherEntityListMapper
import timber.log.Timber

class LoadDataHistory(
    private val weatherDao: WeatherDao,
    private val mapper: WeatherEntityListMapper,
) : UseCase<Unit, List<WeatherData>>() {

    override suspend fun run(params: Unit): Either<String, List<WeatherData>> =
        try {
            weatherDao.get().reversed()
                .run {
                    Either.Right(mapper(this))
                }
        } catch (e: Exception) {
            Timber.w(e, "LoadDataHistory failure")
            Either.Left(e.localizedMessage ?: "unknown error")
        }
}