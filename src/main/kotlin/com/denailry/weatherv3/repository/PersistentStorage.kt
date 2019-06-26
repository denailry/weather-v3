package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel
import com.mocked.database.Database
import com.mocked.database.Weather
import com.mocked.database.WeatherDay
import java.lang.Exception

class PersistentStorage(private val database: Database) : Repository {
    override fun create(model: WeatherModel) {
        val weather = createWeatherFrom(model) ?: throw Exception("invalid model")
        database.save(weather)
    }

    override fun read(location: String): List<WeatherModel> {
        return ArrayList()
    }

    override fun update(model: WeatherModel) {}

    override fun delete(model: WeatherModel) {}

    private fun createWeatherFrom(model: WeatherModel) : Weather? {
        for (day in WeatherDay.values()) {
            if (model.day.toString() == day.toString()) {
                return Weather(model.location, day, model.temperature, model.type)
            }
        }
        return null
    }
}