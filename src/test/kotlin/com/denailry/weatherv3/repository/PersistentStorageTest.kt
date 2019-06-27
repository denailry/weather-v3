package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel
import com.mocked.database.Database
import com.mocked.database.Weather
import com.mocked.database.WeatherDay
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doNothing
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class PersistentStorageTest {
    @Mock
    lateinit var storage: Database

    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `given weather model when saving then saved weather should me identical to weather model`() {
        val captor = argumentCaptor<Weather>()
        doNothing().`when`(storage).save(captor.capture())

        val database = PersistentStorage(storage)
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")

        database.create(model)

        assertEquals(model.location, captor.firstValue.location)
        assertEquals(model.day.toString(), captor.firstValue.day.toString())
    }

    @Test
    fun `given location when read is called then return all saved weather models corresponding to location`() {
        val location = "jakarta"
        val weathers = arrayListOf(
            Weather(location, WeatherDay.SUNDAY, 5.0f, "shiny"),
            Weather(location, WeatherDay.MONDAY, 5.0f, "shiny"),
            Weather(location, WeatherDay.TUESDAY, 5.0f, "shiny")
        )
        Mockito.`when`(storage.getByLocation(any())).thenReturn(weathers)

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
        val captor = argumentCaptor<Weather>()
        doNothing().`when`(storage).save(captor.capture())

        val database = PersistentStorage(storage)
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")

        database.update(model)

        assertEquals(model.location, captor.firstValue.location)
        assertEquals(model.day.toString(), captor.firstValue.day.toString())
    }

    @Test
    fun `given weather model when delete is called then location and day passed should be identical to model`() {
        val locationCaptor = argumentCaptor<String>()
        val dayCaptor = argumentCaptor<WeatherDay>()
        doNothing().`when`(storage).delete(locationCaptor.capture(), dayCaptor.capture())

        val database = PersistentStorage(storage)
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")

        database.delete(model)

        assertEquals(model.location, locationCaptor.firstValue)
        assertEquals(model.day.toString(), dayCaptor.firstValue.toString())
    }
}