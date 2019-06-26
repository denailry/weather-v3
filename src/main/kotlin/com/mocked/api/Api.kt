package com.mocked.api

class Api {
    fun requestPost(body: List<String>) : Boolean = true

    fun requstGet(location: String, day: String) : List<String> {
        return listOf(location, day, "30.5", "shiny")
    }

    fun requestPut(body: List<String>) : Boolean = true

    fun requestDelete(location: String, day: String) : Boolean = true
}