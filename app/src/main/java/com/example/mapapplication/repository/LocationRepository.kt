package com.example.mapapplication.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.example.mapapplication.model.LocationPermissionListener

class LocationRepository(private val context: Context) {
    val locationPermissionListener = LocationPermissionListener(context)
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }
}