package com.example.mapapplication.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.example.mapapplication.model.LocationPermissionListener

class LocationRepository(private val context: Context) {
    val locationPermissionListener = LocationPermissionListener(context)
    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }
}