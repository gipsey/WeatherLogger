package edu.davidd.weatherlogger.domain.model

import edu.davidd.weatherlogger.data.network.model.WeatherDto
import edu.davidd.weatherlogger.data.persistence.model.WeatherEntity

class WeatherDtoMapper {

    operator fun invoke(timInMillis: Long, dto: WeatherDto) =
        dto.run {
            WeatherEntity(
                timInMillis,
                main.temp,
                name,
                coord.lat,
                coord.lon
            )
        }
}