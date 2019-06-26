package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel
import com.mocked.cache.Cache
import com.mocked.cache.Weather

class CacheStorage(val cache: Cache) : Repository {
    override fun create(model: WeatherModel) {
        val key = model.location + ":" + model.day.toString()
        val weather = Weather(model.location, model.type, model.temperature.toString(), model.day.toString())

        cache.save(key, weather)
    }

    override fun read(location: String): List<WeatherModel> {
        return ArrayList()
    }

    override fun update(model: WeatherModel) {}

    override fun delete(model: WeatherModel) {}
}