package com.example.mapapplication.model

import com.google.gson.annotations.SerializedName

class Place {
    var id: String = ""
    @SerializedName("place_id")
    var placeId: String = ""
    var name = ""
    var icon = ""
    var vicinity = ""
    var geometry:Geometry?=null

    inner class Geometry {
        var location: Location? = null
    }

    inner class Location {
        var lat: Double = 0.0
        var lng: Double = 0.0
    }
}