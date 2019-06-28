package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel
import com.mocked.cache.Cache
import com.mocked.database.Database

class SmartRepository(cacheStorage: Cache, persistentStorage: Database) : Repository {
    private val cache = CacheRepository(cacheStorage)
    private val database = PersistentRepository(persistentStorage)

    override fun create(model: WeatherModel) {
        cache.create(model)
        database.create(model)
    }

    override fun read(location: String): List<WeatherModel> {
        val results = cache.read(location)

        if (results.size < 7) {
            return database.read(location)
        }

        return results
    }

    override fun update(model: WeatherModel) {
        cache.update(model)
        database.update(model)
    }

    override fun delete(model: WeatherModel) {}
}