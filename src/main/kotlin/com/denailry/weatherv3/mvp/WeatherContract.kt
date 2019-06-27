package com.denailry.weatherv3.mvp

interface WeatherContract {
    interface Presenter {
        fun searchWeathersByLocationAndDay(location: String, day: String)
    }

    interface View {
        fun showWeathers(weathers: List<WeatherModel>)
    }
}