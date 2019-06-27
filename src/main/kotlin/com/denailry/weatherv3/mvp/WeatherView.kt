package com.denailry.weatherv3.mvp

import java.io.PrintStream

class WeatherView(private val printer: PrintStream) : WeatherContract.View {
    constructor() : this(System.out)

    private var presenter: WeatherContract.Presenter? = null

    fun acceptLocationAndDay(location: String, day: String) {
        presenter?.searchWeathersByLocationAndDay(location, day)
    }

    fun setPresenter(presenter: WeatherContract.Presenter) {
        this.presenter = presenter
    }

    override fun showWeathers(weathers: List<WeatherModel>) {
        weathers.forEach { printer.println(it) }
    }
}