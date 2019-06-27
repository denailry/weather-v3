package com.denailry.weatherv3.mvp

import com.mocked.database.Weather
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class WeatherViewTest {
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
}