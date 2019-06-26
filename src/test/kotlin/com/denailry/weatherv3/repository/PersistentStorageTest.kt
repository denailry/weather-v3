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
        var weathers: List<Weather> = ArrayList()

        override fun clean() {}

        override fun save(newWeather: Weather) {
            lastSavedWeather = newWeather
        }

        override fun getByLocation(location: String): List<Weather> {
            return weathers
        }

        override fun delete(location: String, day: WeatherDay) {
            lastSavedWeather = Weather(location, day, 25.5f, "")
        }
    }

    @Test
    fun `given weather model when saving then saved weather should me identical to weather model`() {
        val database = PersistentStorage(storage)
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")

        database.create(model)

        assertEquals(model.location, storage.lastSavedWeather?.location)
        assertEquals(model.day.toString(), storage.lastSavedWeather?.day.toString())
    }

    @Test
    fun `given location when read is called then return all saved weather models corresponding to location`() {
        val location = "jakarta"
        val weathers = arrayListOf<Weather>(
            Weather(location, WeatherDay.SUNDAY, 5.0f, "shiny"),
            Weather(location, WeatherDay.MONDAY, 5.0f, "shiny"),
            Weather(location, WeatherDay.TUESDAY, 5.0f, "shiny")
        )
        storage.weathers = weathers

        val database = PersistentStorage(storage)
        val results = database.read(location)

        assertEquals(weathers.size, results.size)
        for (result in results) {
            for (i in 0 until weathers.size) {
                val weather = weathers[i]
                if (result.location == weather.location && result.day.toString() == weather.day.toString()) {
                    weathers.removeAt(i)
                    break
                }
            }
        }
        assertEquals(0, weathers.size)
    }

    @Test
    fun `given weather model when update is called then identical weather corresponding to model should be saved`() {
        val database = PersistentStorage(storage)
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")

        database.update(model)

        assertEquals(model.location, storage.lastSavedWeather?.location)
        assertEquals(model.day.toString(), storage.lastSavedWeather?.day.toString())
    }

    @Test
    fun `given weather model when delete is called then location and day passed should be identical to model`() {
        val database = PersistentStorage(storage)
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")

        database.delete(model)

        assertEquals(model.location, storage.lastSavedWeather?.location)
        assertEquals(model.day.toString(), storage.lastSavedWeather?.day.toString())
    }
}