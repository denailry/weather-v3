package com.denailry.mocked.database

interface Database {
    fun clean()
    fun save(newWeather: Weather)
    fun getByLocation(location: String) : List<Weather>
    fun delete(location: String, day: WeatherDay)
}