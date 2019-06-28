package com.denailry.mocked.api

interface API {
    fun requestPost(body: List<String>) : Boolean
    fun requestGet(location: String, day: String) : List<String>
    fun requestPut(body: List<String>) : Boolean
    fun requestDelete(location: String, day: String) : Boolean
}