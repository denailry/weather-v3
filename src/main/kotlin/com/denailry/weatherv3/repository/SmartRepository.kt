package com.denailry.weatherv3.repository

import com.denailry.weatherv3.mvp.WeatherModel
import com.denailry.mocked.cache.Cache
import com.denailry.mocked.database.Database

class SmartRepository(cacheStorage: Cache, persistentStorage: Database) : Repository {
    private val cache = CacheRepository(cacheStorage)
    private val database = PersistentRepository(persistentStorage)

    override fun create(model: WeatherModel) {
        cache.create(model)
        database.create(model)
    }

    override fun read(location: String): List<WeatherModel> {
        var results = cache.read(location)

        if (results.size < 7) {
            results = database.read(location)
            for (result in results) {
                cache.create(result)
            }
        }

        return results
    }

    override fun update(model: WeatherModel) {
        cache.update(model)
        database.update(model)
    }

    override fun delete(model: WeatherModel) {
        cache.delete(model)
        database.delete(model)
    }
}