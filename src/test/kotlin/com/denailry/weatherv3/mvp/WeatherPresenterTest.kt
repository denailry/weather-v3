package com.denailry.weatherv3.mvp

import com.denailry.weatherv3.repository.Repository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doNothing
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class WeatherPresenterTest {
    @Mock
    lateinit var view : WeatherContract.View

    @Mock
    lateinit var repository: Repository

    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `given location and day when searching by given data then presenter should return data received from repository`() {
        val location = "jakarta"
        val models : ArrayList<WeatherModel> = arrayListOf(
            WeatherModel(location, WeatherModel.Day.MONDAY, 25.0f, "shiny"),
            WeatherModel(location, WeatherModel.Day.TUESDAY, 25.0f, "rainy")
        )
        Mockito.`when`(repository.read(any())).thenReturn(models.toList())

        val weathersCaptor = argumentCaptor<List<WeatherModel>>()
        doNothing().`when`(view).showWeathers(weathersCaptor.capture())

        val presenter = WeatherPresenter(view, repository)
        presenter.searchWeathersByLocationAndDay("jakarta", "monday")

        assertEquals(models.size, weathersCaptor.firstValue.size)
        for (showedModel in weathersCaptor.firstValue) {
            for (i in 0 until models.size) {
                val model = models[i]
                if (model.location == showedModel.location && model.day == showedModel.day) {
                    models.removeAt(i)
                    break
                }
            }
        }
        assertEquals(0, models.size)
    }

    @Test
    fun `given location and day when searching by given data then give only 5 consecutive day of weather to view`() {
        val location = "jakarta"
        val models : ArrayList<WeatherModel> = arrayListOf(
            WeatherModel(location, WeatherModel.Day.SATURDAY, 25.0f, "rainy"),
            WeatherModel(location, WeatherModel.Day.SUNDAY, 25.0f, "rainy"),
            WeatherModel(location, WeatherModel.Day.WEDNESDAY, 25.0f, "rainy"),
            WeatherModel(location, WeatherModel.Day.THURSDAY, 25.0f, "rainy"),
            WeatherModel(location, WeatherModel.Day.MONDAY, 25.0f, "shiny"),
            WeatherModel(location, WeatherModel.Day.TUESDAY, 25.0f, "rainy"),
            WeatherModel(location, WeatherModel.Day.FRIDAY, 25.0f, "rainy")
        )
        Mockito.`when`(repository.read(any())).thenReturn(models.toList())

        val weathersCaptor = argumentCaptor<List<WeatherModel>>()
        doNothing().`when`(view).showWeathers(weathersCaptor.capture())

        val presenter = WeatherPresenter(view, repository)
        presenter.searchWeathersByLocationAndDay("jakarta", "monday")

        val showedWeathers = weathersCaptor.firstValue
        assertEquals(5, showedWeathers.size)
        assertEquals(WeatherModel.Day.MONDAY, showedWeathers[0].day)
        assertEquals(WeatherModel.Day.TUESDAY, showedWeathers[1].day)
        assertEquals(WeatherModel.Day.WEDNESDAY, showedWeathers[2].day)
        assertEquals(WeatherModel.Day.THURSDAY, showedWeathers[3].day)
        assertEquals(WeatherModel.Day.FRIDAY, showedWeathers[4].day)
    }
}