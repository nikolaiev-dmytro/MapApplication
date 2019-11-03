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


    @GET("/maps/api/directions/json")
    fun getDirection(@Query("key") apiKey: String, @Query("origin") origin: String, @Query("destination") destination: String): Observable<JsonObject>

    @GET("/maps/api/place/nearbysearch/json?rankby=distance&type=restaurant")
    fun getNearbyPlaces(@Query("key") apiKey: String, @Query("location") location: String): Observable<JsonObject>
}