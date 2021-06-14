package edu.davidd.weatherlogger.data.persistence

import android.content.SharedPreferences
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import edu.davidd.weatherlogger.data.persistence.model.WeatherEntity

class WeatherDaoPreferences(
    private val preferences: SharedPreferences,
    private val moshi: Moshi
) : WeatherDao {

    @Throws(Throwable::class)
    override fun save(list: List<WeatherEntity>): Boolean {
        val json = createJsonAdapter().toJson(list)
        return preferences.edit().putString(KEY_WEATHER_ITEMS_JSON, json).commit()
    }

    @Throws(Throwable::class)
    override fun get(): List<WeatherEntity> =
        preferences.getString(KEY_WEATHER_ITEMS_JSON, null)
            ?.let { json ->
                createJsonAdapter().fromJson(json)
            } ?: emptyList()

    private fun createJsonAdapter(): JsonAdapter<List<WeatherEntity>> =
        moshi.adapter(Types.newParameterizedType(List::class.java, WeatherEntity::class.java))

    companion object {
        private const val KEY_WEATHER_ITEMS_JSON = "items"
    }
}