package com.denailry.weatherv3.mvp

import java.io.PrintStream

class WeatherView(private val printer: PrintStream) : WeatherContract.View {
    override fun showWeathers(weathers: List<WeatherModel>) {
        weathers.forEach { printer.println(it) }
    }
}