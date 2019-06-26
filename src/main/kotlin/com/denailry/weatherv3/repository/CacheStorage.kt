package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel
import com.mocked.cache.Cache
import com.mocked.cache.Weather

class CacheStorage(private val cache: Cache) : Repository {
    override fun create(model: WeatherModel) {
        val key = "${model.location}:${model.day}"
        cache.save(key, createCachedWeatherFromModel(model))
    }

    override fun read(location: String): List<WeatherModel> {
        val results = ArrayList<WeatherModel>()

        for (day in WeatherModel.Day.values()) {
            val weather = cache.get("$location:$day") ?: continue
            val model = createModel(weather) ?: continue
            results.add(model)
        }

        return results
    }

    override fun update(model: WeatherModel) {}

    override fun delete(model: WeatherModel) {
        cache.save("${model.location}:${model.day}", null)
    }

    private fun createModel(weather: Weather) : WeatherModel? {
        for (day in WeatherModel.Day.values()) {
            if (weather.day == day.toString()) {
                return WeatherModel(weather.location, day, weather.temperature.toFloat(), weather.type)
            }
        }
        return null
    }

    private fun createCachedWeatherFromModel(model: WeatherModel) : Weather {
        return Weather(model.location, model.type, model.temperature.toString(), model.day.toString())
    }
}