package com.mocked.database

import java.io.File
import java.io.PrintStream
import java.util.*
import kotlin.collections.ArrayList

class GoSQL(val source: String) : Database {
    override fun clean() {
        val weathers = collectWeatherFromFile()
        persist(weathers)
    }

    override fun save(newWeather: Weather) {
        val weathers = collectWeatherFromFile()
        val newWeathers = ArrayList<Weather>()

        for (weather in weathers) {
            if (weather.location == newWeather.location && weather.day == newWeather.day) {
                newWeathers.add(newWeather)
            } else {
                newWeathers.add(weather)
            }
        }

        persist(newWeathers)
    }

    override fun getByLocation(location: String) : List<Weather> {
        val weathers = collectWeatherFromFile()

        val results = ArrayList<Weather>()

        for (weather in weathers) {
            if (weather.location == location) {
                results.add(weather)
            }
        }

        return results
    }

    override fun delete(location: String, day: WeatherDay) {
        val weathers = collectWeatherFromFile()
        val newWeathers = ArrayList<Weather>()

        for (weather in weathers) {
            if (weather.location != location || weather.day != day) {
                newWeathers.add(weather)
            }
        }

        persist(newWeathers)
    }

    private fun collectWeatherFromFile() : List<Weather> {
        val weathers = ArrayList<Weather>()

        val scanner = Scanner(File(source))

        while (scanner.hasNext()) {
            val row = scanner.nextLine()

            if (row != null) {
                val attributes = row.split(";")
                val weather = createWeather(attributes)

                if (weather != null) {
                    weathers.add(weather)
                }
            }
        }

        return weathers
    }

    private fun persist(weathers: List<Weather>) {
        val ps = PrintStream(File(source))

        for (weather in weathers) {
            ps.println(weather.toString())
        }
    }

    private fun createWeather(attributes: List<String>) : Weather? {
        return WeatherBuilder()
            .setLocation(attributes[0])
            .setDay(attributes[1])
            .setTemperature(attributes[2])
            .setType(attributes[3])
            .build()
    }
}