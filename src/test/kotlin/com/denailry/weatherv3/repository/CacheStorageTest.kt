package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel
import com.mocked.cache.Cache
import com.mocked.cache.Weather
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CacheStorageTest {
    private var storage = object : Cache {
        var lastKey: String = ""
            private set
        var lastWeather: Weather? = Weather("", "", "", "")
            private set
        var savedWeathers = HashMap<String, Weather>()

        override fun save(key: String, weather: Weather?) {
            lastKey = key
            lastWeather = weather
        }

        override fun get(key: String): Weather? {
            return savedWeathers[key]
        }

        fun setSavedWeathers(weathers: List<Weather>) {
            savedWeathers = HashMap()
            weathers.forEach { savedWeathers["${it.location}:${it.day}"] = it }
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

    @Test
    fun `given location when read is called then return all saved weathers of that location no less or more`() {
        val location = "jakarta"
        val weathers = arrayListOf<Weather>(
            Weather(location, "x", "0.0", "monday"),
            Weather(location, "x", "0.0", "friday"),
            Weather(location, "x", "0.0", "tuesday")
        )
        storage.setSavedWeathers(weathers)

        val cache = CacheStorage(storage)
        val results = cache.read(location)

        assertEquals(weathers.size, results.size)
        for (result in results) {
            for (i in 0 until weathers.size) {
                val weather = weathers[i]
                if (result.location == weather.location && result.day.toString() == weather.day) {
                    weathers.removeAt(i)
                    break
                }
            }
        }
        assertEquals(0, weathers.size)
    }

    @Test
    fun `given weather when delete is called then null should be saved on corresponding location-day pair`() {
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")
        val cache = CacheStorage(storage)

        cache.delete(model)

        assertEquals("${model.location}:${model.day}", storage.lastKey)
        assertNull(storage.lastWeather)
    }

    @Test
    fun `given weather when update is called then weather and corresponding key should be saved to storage`() {
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")
        val cache = CacheStorage(storage)

        cache.update(model)

        assertEquals("${model.location}:${model.day}", storage.lastKey)
        assertEquals(model.location, storage.lastWeather?.location)
        assertEquals(model.day.toString(), storage.lastWeather?.day)
    }
}