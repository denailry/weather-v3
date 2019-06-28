package com.denailry.mocked.database

class WeatherBuilder {
    private var location: String? = null
    private var day: String? = null
    private var temperature: String? = null
    private var type: String? = null

    fun setLocation(location: String) : WeatherBuilder {
        this.location = location
        return this
    }

    fun setDay(day: String) : WeatherBuilder {
        this.day = day
        return this
    }

    fun setTemperature(temperature: String) : WeatherBuilder {
        this.temperature = temperature
        return this
    }

    fun setType(type: String) : WeatherBuilder {
        this.type = type
        return this
    }

    fun build() : Weather? {
        if (location == null || type == null) return null

        val finalLocation = location ?: return null
        val finalDay = selectDay(day) ?: return null
        val finalTemperature = temperature?.toFloatOrNull() ?: return null
        val finalType = type ?: return null

        return Weather(finalLocation, finalDay, finalTemperature, finalType)
    }

    private fun selectDay(day: String?) : WeatherDay? {
        when (day) {
            "monday" -> return WeatherDay.MONDAY
            "tuesday" -> return WeatherDay.TUESDAY
            "wednesday" -> return WeatherDay.WEDNESDAY
            "thursday" -> return WeatherDay.THURSDAY
            "friday" -> return WeatherDay.FRIDAY
            "saturday" -> return WeatherDay.SATURDAY
            "sunday" -> return WeatherDay.SUNDAY
        }
        return null
    }
}