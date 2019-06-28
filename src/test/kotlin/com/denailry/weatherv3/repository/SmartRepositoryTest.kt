package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel
import com.mocked.cache.Cache
import com.mocked.cache.Weather as CacheWeather
import com.mocked.database.Weather as DatabaseWeather
import com.mocked.database.Database
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SmartRepositoryTest {
    @Mock
    lateinit var cache: Cache

    @Mock
    lateinit var database: Database

    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `given weather when saving then weather should be saved in cache and database`() {
        val keyCaptor = argumentCaptor<String>()
        val cacheWeatherCaptor = argumentCaptor<CacheWeather>()
        doNothing().`when`(cache).save(keyCaptor.capture(), cacheWeatherCaptor.capture())

        val databaseWeatherCaptor = argumentCaptor<DatabaseWeather>()
        doNothing().`when`(database).save(databaseWeatherCaptor.capture())

        val repo = SmartRepository(cache, database)
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")

        repo.create(model)

        assertEquals("${model.location}:${model.day}", keyCaptor.firstValue)
        assertEquals(model.location, cacheWeatherCaptor.firstValue.location)
        assertEquals(model.day.toString(), cacheWeatherCaptor.firstValue.day)
        assertEquals(model.location, databaseWeatherCaptor.firstValue.location)
        assertEquals(model.day.toString(), databaseWeatherCaptor.firstValue.day.toString())
    }

    @Test
    fun `given location when data in cache is not complete then weather should be fetched from database`() {
        val location = "jakarta"

        val repo = SmartRepository(cache, database)

        repo.read(location)

        verify(cache, times(7)).get(any())
        verify(database).getByLocation(any())
    }
}