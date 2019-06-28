package com.denailry.weatherv3.mvp

data class WeatherModel(val location: String, val day: Day, val temperature: Float, val type: String) {
    enum class Day() {
        MONDAY() {
            override fun toString(): String = "monday"
        },
        TUESDAY() {
            override fun toString(): String = "tuesday"
        },
        WEDNESDAY() {
            override fun toString(): String = "wednesday"
        },
        THURSDAY() {
            override fun toString(): String = "thursday"
        },
        FRIDAY() {
            override fun toString(): String = "friday"
        },
        SATURDAY() {
            override fun toString(): String = "saturday"
        },
        SUNDAY() {
            override fun toString(): String = "sunday"
        }
    }
}