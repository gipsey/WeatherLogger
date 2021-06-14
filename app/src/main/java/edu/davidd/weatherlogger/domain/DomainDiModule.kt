package edu.davidd.weatherlogger.domain

import edu.davidd.weatherlogger.domain.model.WeatherDtoMapper
import edu.davidd.weatherlogger.domain.model.WeatherEntityListMapper
import edu.davidd.weatherlogger.domain.usecase.DownloadDataForLocation
import edu.davidd.weatherlogger.domain.usecase.LoadDataHistory
import org.koin.dsl.module

val domainDiModule = module {

    factory { DownloadDataForLocation(get(), get(), get(), get()) }
    factory { LoadDataHistory(get(), get()) }

    factory { WeatherEntityListMapper() }
    factory { WeatherDtoMapper() }
}