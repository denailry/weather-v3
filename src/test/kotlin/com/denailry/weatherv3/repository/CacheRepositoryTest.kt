package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel
import com.mocked.cache.Cache
import com.mocked.cache.Weather
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doNothing
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CacheRepositoryTest {
    @Mock
    lateinit var storage: Cache

    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `given weather when saving then key should be composed from location and day`() {
        val keyCaptor = argumentCaptor<String>()
        val weatherCaptor = argumentCaptor<Weather>()
        doNothing().`when`(storage).save(keyCaptor.capture(), weatherCaptor.capture())

        val cache = CacheRepository(storage)
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")

        cache.create(model)

        assertEquals("${model.location}:${model.day}", keyCaptor.firstValue)
        assertEquals(model.location, weatherCaptor.firstValue.location)
        assertEquals(model.day.toString(), weatherCaptor.firstValue.day)
    }

    @Test
    fun `given location when read is called then return all saved weathers of that location no less or more`() {
        val location = "jakarta"
        val weathers = arrayListOf(
            Weather(location, "x", "0.0", "monday"),
            Weather(location, "x", "0.0", "friday"),
            Weather(location, "x", "0.0", "tuesday")
        )

        doAnswer {
            when {
                it.arguments[0].toString() == "$location:${weathers[0].day}" -> return@doAnswer weathers[0]
                it.arguments[0].toString() == "$location:${weathers[1].day}" -> return@doAnswer weathers[1]
                it.arguments[0].toString() == "$location:${weathers[2].day}" -> return@doAnswer weathers[2]
            }
            return@doAnswer null
        }.`when`(storage).get(any())

        val cache = CacheRepository(storage)
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
        val keyCaptor = argumentCaptor<String>()
        val weatherCaptor = argumentCaptor<Weather>()
        doNothing().`when`(storage).save(keyCaptor.capture(), weatherCaptor.capture())

        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")
        val cache = CacheRepository(storage)

        cache.delete(model)

        assertEquals("${model.location}:${model.day}", keyCaptor.firstValue)
        assertNull(weatherCaptor.firstValue)
    }

    @Test
    fun `given weather when update is called then weather and corresponding key should be saved to storage`() {
        val keyCaptor = argumentCaptor<String>()
        val weatherCaptor = argumentCaptor<Weather>()
        doNothing().`when`(storage).save(keyCaptor.capture(), weatherCaptor.capture())

        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")
        val cache = CacheRepository(storage)

        cache.update(model)

        assertEquals("${model.location}:${model.day}", keyCaptor.firstValue)
        assertEquals(model.location, weatherCaptor.firstValue.location)
        assertEquals(model.day.toString(), weatherCaptor.firstValue.day)
    }
}