package com.denailry.weatherv3.mvp

import com.denailry.weatherv3.repository.Repository
import kotlin.math.max
import kotlin.math.min

class WeatherPresenter(private val view: WeatherContract.View, private val repo: Repository) : WeatherContract.Presenter {
    companion object {
        const val MAX_SHOWED_WEATHER = 5
    }

    override fun searchWeathersByLocationAndDay(location: String, day: String) {
        val weathers = repo.read(location)
        view.showWeathers(filterWeatherByDay(weathers, day))
    }

    private fun filterWeatherByDay(unsortedWeathers: List<WeatherModel>, day: String) : List<WeatherModel> {
        val weathers = unsortedWeathers.sortedWith(compareBy{it.day})

        val startIndex = findIndexOfDay(weathers, day) ?: return ArrayList()

        val results = ArrayList<WeatherModel>()
        val maxItem = min(MAX_SHOWED_WEATHER, weathers.size)

        var i = startIndex
        while (results.size < maxItem) {
            results.add(weathers[i])
            i = (i + 1) % weathers.size
        }

        return results
    }

    private fun findIndexOfDay(weathers: List<WeatherModel>, rawDay: String) : Int? {
        val day = parseDay(rawDay) ?: null

        for ((index, weather) in weathers.withIndex()) {
            if (weather.day == day) {
                return index
            }
        }

        return null
    }

    private fun parseDay(day: String) : WeatherModel.Day? {
        when (day) {
            "monday" -> return WeatherModel.Day.MONDAY
            "tuesday" -> return WeatherModel.Day.TUESDAY
            "wednesday" -> return WeatherModel.Day.WEDNESDAY
            "thursday" -> return WeatherModel.Day.THURSDAY
            "friday" -> return WeatherModel.Day.FRIDAY
            "saturday" -> return WeatherModel.Day.SATURDAY
            "sunday" -> return WeatherModel.Day.SUNDAY
        }

        return null
    }
}