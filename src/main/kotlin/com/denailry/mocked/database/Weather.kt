package com.denailry.mocked.database

data class Weather(val location: String, val day: WeatherDay, val temperature: Float, val type: String) {
    override fun toString(): String {
        return "$location;$day;$temperature;$type"
    }
}