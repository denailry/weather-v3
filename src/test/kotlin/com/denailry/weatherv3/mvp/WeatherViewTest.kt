package com.denailry.weatherv3.mvp

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doNothing
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class WeatherViewTest {
    @Mock
    lateinit var presenter: WeatherContract.Presenter

    @BeforeEach
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    private var output = ByteArrayOutputStream()
    private var printer = PrintStream(output)

    @Test
    fun `given list of weathers when showWeathers is called then view should print the weather`() {
        val models : ArrayList<WeatherModel> = arrayListOf(
            WeatherModel("jakarta", WeatherModel.Day.TUESDAY, 25.5f, "shiny"),
            WeatherModel("jakarta", WeatherModel.Day.MONDAY, 26.4f, "rainy")
        )

        val view = WeatherView(printer)
        view.showWeathers(models)

        val results = output.toString().split(System.lineSeparator())

        for (result in results) {
            for (i in 0 until models.size) {
                val model = models[i]
                if (result.toString() == model.toString()) {
                    models.removeAt(i)
                    break
                }
            }
        }
        assertEquals(0, models.size)
    }

    @Test
    fun `given location and day when accepting given data then view should tell presenter to search weathers`() {
        val locationCaptor = argumentCaptor<String>()
        val dayCaptor = argumentCaptor<String>()
        doNothing().`when`(presenter).searchWeathersByLocationAndDay(locationCaptor.capture(), dayCaptor.capture())

        val location = "jakarta"
        val day = "monday"

        val view = WeatherView()
        view.setPresenter(presenter)
        view.acceptLocationAndDay(location, day)

        assertEquals(location, locationCaptor.firstValue)
        assertEquals(day, dayCaptor.firstValue)
    }
}