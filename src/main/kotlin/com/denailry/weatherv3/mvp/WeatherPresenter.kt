package com.denailry.weatherv3.mvp

import com.denailry.weatherv3.repository.Repository

class WeatherPresenter(private val view: WeatherContract.View, private val repo: Repository) : WeatherContract.Presenter {
    override fun searchWeathersByLocationAndDay(location: String, day: String) {
        val weathers = repo.read(location)
        view.showWeathers(weathers)
    }
}