package com.example.mapapplication.repository

import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    companion object {
        const val BASE_URL = "https://maps.googleapis.com/maps/"
        const val API_KEY = "AIzaSyBPWFE6j27jZiEtEhYIOpnxEf-7CRC9kDY"
    }

    @GET("/maps/api/place/nearbysearch/json?rankby=distance")
    fun getNearbyPlaces(@Query("key") apiKey: String, @Query("location") location: String)
}