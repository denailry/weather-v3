package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel
import com.mocked.cache.Cache
import com.mocked.cache.Weather
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CacheStorageTest {
    private val storage = object : Cache {
        var lastKey: String = ""
            private set
        var lastWeather: Weather? = null
            private set

        override fun save(key: String, weather: Weather?) {
            lastKey = key
            lastWeather = weather
        }

        override fun get(key: String): Weather? {
            return Weather("someplace", "shiny", "25.5", "shiny")
        }
    }

    @Test
    fun `given weather when saving then key should be composed from location and day`() {
        val cache = CacheStorage(storage)
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")

        cache.create(model)

        val expected = model.location + ":" + model.day.toString()

        assertEquals(expected, storage.lastKey)
    }
}