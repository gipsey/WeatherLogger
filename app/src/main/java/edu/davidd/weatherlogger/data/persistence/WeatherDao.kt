package edu.davidd.weatherlogger.data.persistence

import edu.davidd.weatherlogger.data.persistence.model.WeatherEntity

interface WeatherDao {

    fun save(list: List<WeatherEntity>): Boolean
    fun get(): List<WeatherEntity>
}