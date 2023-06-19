package com.example.starsname.api

import retrofit2.http.GET
import retrofit2.http.Query

interface APODApi {
    @GET("planetary/apod")
    suspend fun getAPOD(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): APODResponse
}