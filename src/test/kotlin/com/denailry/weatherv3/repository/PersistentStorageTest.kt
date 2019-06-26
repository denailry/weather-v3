package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel
import com.mocked.database.Database
import com.mocked.database.Weather
import com.mocked.database.WeatherDay
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PersistentStorageTest {
    private val storage = object : Database {
        var lastSavedWeather: Weather? = null
            private set

        override fun clean() {}

        override fun save(newWeather: Weather) {
            lastSavedWeather = newWeather
        }

        override fun getByLocation(location: String): List<Weather> {
            return ArrayList()
        }

        override fun delete(location: String, day: WeatherDay) {}
    }

    @Test
    fun `given weather model when saving then saved weather should me identical to weather model`() {
        val database = PersistentStorage(storage)
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")

        database.create(model)

        assertEquals(model.location, storage.lastSavedWeather?.location)
        assertEquals(model.day.toString(), storage.lastSavedWeather?.day.toString())
    }
}