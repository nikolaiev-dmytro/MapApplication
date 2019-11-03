package com.example.mapapplication.repository

import com.google.gson.JsonObject
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    companion object {
        const val BASE_URL = "https://maps.googleapis.com/maps/"
    }

    @GET("/maps/api/place/nearbysearch/json?rankby=distance")
    fun getNearbyPlaces(@Query("key") apiKey: String, @Query("location") location: String):Observable<JsonObject>
}