package com.denailry.weatherv3.mvp

import com.denailry.weatherv3.repository.Repository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WeatherPresenterTest {
    private val view = object : WeatherContract.View {
        var showedWeathers : List<WeatherModel> = ArrayList()
            private set

        override fun showWeathers(weathers: List<WeatherModel>) {
            showedWeathers = weathers
        }
    }

    private val repository = object : Repository {
        var savedWeathers: List<WeatherModel> = ArrayList()

        override fun create(model: WeatherModel) {}

        override fun read(location: String): List<WeatherModel> {
            return savedWeathers.toList()
        }

        override fun update(model: WeatherModel) {}

        override fun delete(model: WeatherModel) {}
    }

    @Test
    fun `given location and day when searching by given data then presenter should returned dat received from repository`() {
        val location = "jakarta"
        val models : ArrayList<WeatherModel> = arrayListOf(
            WeatherModel(location, WeatherModel.Day.MONDAY, 25.0f, "shiny"),
            WeatherModel(location, WeatherModel.Day.TUESDAY, 25.0f, "rainy")
        )
        repository.savedWeathers = models

        val presenter = WeatherPresenter(view, repository)
        presenter.searchWeathersByLocationAndDay("jakarta", "monday")

        assertEquals(models.size, view.showedWeathers.size)
        for (showedModel in view.showedWeathers) {
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
}