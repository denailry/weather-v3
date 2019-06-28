package com.denailry.mocked.cache

class GoCache : Cache {
    companion object {
        private const val EXPIRATION_IN_MILLISECONDS = 5000
    }

    private val weathersByLoction: HashMap<String, CachedWeather?> = HashMap()

    override fun save(key: String, weather: Weather?) {
        if (weather == null) {
            weathersByLoction[key] = null
        } else {
            weathersByLoction[key] =
                CachedWeather(weather, System.currentTimeMillis())
        }
    }

    override fun get(key: String) : Weather? {
        val cachedWeather = weathersByLoction[key] ?: return null

        if ((System.currentTimeMillis() - cachedWeather.createdAt) > EXPIRATION_IN_MILLISECONDS) {
            save(key, null)
            return null
        }

        return cachedWeather.weather
    }

    private data class CachedWeather(val weather: Weather, val createdAt: Long)
}