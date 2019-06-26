package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel

interface Repository {
    fun create(model: WeatherModel)
    fun read(location: String) : List<WeatherModel>
    fun update(model: WeatherModel)
    fun delete(model: WeatherModel)
}