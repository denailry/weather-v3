package com.mocked.database

enum class WeatherDay(value: Int) {
    MONDAY(0) {
        override fun toString(): String = "monday"
    },
    TUESDAY(1) {
        override fun toString(): String = "tuesday"
    },
    WEDNESDAY(2) {
        override fun toString(): String = "wednesday"
    },
    THURSDAY(3) {
        override fun toString(): String = "thursday"
    },
    FRIDAY(4) {
        override fun toString(): String = "friday"
    },
    SATURDAY(5) {
        override fun toString(): String = "saturday"
    },
    SUNDAY(6) {
        override fun toString(): String = "sunday"
    }
}