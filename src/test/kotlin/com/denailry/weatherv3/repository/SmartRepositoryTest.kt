package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel
import com.denailry.mocked.cache.Cache
import com.denailry.mocked.cache.Weather as CacheWeather
import com.denailry.mocked.database.Weather as DatabaseWeather
import com.denailry.mocked.database.Database
import com.denailry.mocked.database.WeatherDay
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
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

    @Test
    fun `given location when data in cache is complete then database should not be called`() {
        val location = "jakarta"
        val weathers = arrayListOf(
            CacheWeather(location, "x", "0.0", "monday"),
            CacheWeather(location, "x", "0.0", "friday"),
            CacheWeather(location, "x", "0.0", "tuesday"),
            CacheWeather(location, "x", "0.0", "wednesday"),
            CacheWeather(location, "x", "0.0", "saturday"),
            CacheWeather(location, "x", "0.0", "sunday"),
            CacheWeather(location, "x", "0.0", "thursday")
        )
        doAnswer {
            for (i in 0 until weathers.size) {
                if (it.arguments[0].toString() == "$location:${weathers[i].day}") {
                    return@doAnswer weathers[i]
                }
            }
            return@doAnswer null
        }.`when`(cache).get(any())

        val repo = SmartRepository(cache, database)

        val results = repo.read(location)

        verify(cache, times(7)).get(any())
        verify(database, times(0)).getByLocation(any())
        assertEquals(weathers.size, results.size)
    }

    @Test
    fun `given weather when update is called then update should be called in cache and database`() {
        val keyCaptor = argumentCaptor<String>()
        val cacheWeatherCaptor = argumentCaptor<CacheWeather>()
        doNothing().`when`(cache).save(keyCaptor.capture(), cacheWeatherCaptor.capture())

        val databaseWeatherCaptor = argumentCaptor<DatabaseWeather>()
        doNothing().`when`(database).save(databaseWeatherCaptor.capture())

        val repo = SmartRepository(cache, database)
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")

        repo.update(model)

        assertEquals("${model.location}:${model.day}", keyCaptor.firstValue)
        assertEquals(model.location, cacheWeatherCaptor.firstValue.location)
        assertEquals(model.day.toString(), cacheWeatherCaptor.firstValue.day)
        assertEquals(model.location, databaseWeatherCaptor.firstValue.location)
        assertEquals(model.day.toString(), databaseWeatherCaptor.firstValue.day.toString())
    }

    @Test
    fun `given weather when delete is called then delete should be called in cache and database`() {
        val keyCaptor = argumentCaptor<String>()
        val cacheWeatherCaptor = argumentCaptor<CacheWeather>()
        doNothing().`when`(cache).save(keyCaptor.capture(), cacheWeatherCaptor.capture())

        val locationCaptor = argumentCaptor<String>()
        val dayCaptor = argumentCaptor<WeatherDay>()
        doNothing().`when`(database).delete(locationCaptor.capture(), dayCaptor.capture())

        val repo = SmartRepository(cache, database)
        val model = WeatherModel("jakarta", WeatherModel.Day.MONDAY, 25.5f, "shiny")

        repo.delete(model)

        assertEquals("${model.location}:${model.day}", keyCaptor.firstValue)
        Assertions.assertNull(cacheWeatherCaptor.firstValue)
        assertEquals(model.location, locationCaptor.firstValue)
        assertEquals(model.day.toString(), dayCaptor.firstValue.toString())
    }

    @Test
    fun `given location when data in cache is not complete then data from database should be saved in cache`() {
        val location = "jakarta"
        val weathers = arrayListOf(
            DatabaseWeather(location, WeatherDay.MONDAY, 25.3f, "x"),
            DatabaseWeather(location, WeatherDay.TUESDAY, 25.3f, "x"),
            DatabaseWeather(location, WeatherDay.SATURDAY, 25.3f, "x"),
            DatabaseWeather(location, WeatherDay.SUNDAY, 25.3f, "x"),
            DatabaseWeather(location, WeatherDay.WEDNESDAY, 25.3f, "x"),
            DatabaseWeather(location, WeatherDay.THURSDAY, 25.3f, "x"),
            DatabaseWeather(location, WeatherDay.FRIDAY, 25.3f, "x")
        )
        Mockito.`when`(database.getByLocation(any())).thenReturn(weathers)

        val repo = SmartRepository(cache, database)

        repo.read(location)

        verify(cache, times(7)).get(any())
        verify(database).getByLocation(any())
        verify(cache, times(7)).save(any(), any())
    }
}
