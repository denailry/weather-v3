package com.mocked.api

class GoService : API {
    override fun requestPost(body: List<String>) : Boolean = true

    override fun requestGet(location: String, day: String) : List<String> {
        return listOf(location, day, "30.5", "shiny")
    }

    override fun requestPut(body: List<String>) : Boolean = true

    override fun requestDelete(location: String, day: String) : Boolean = true
}