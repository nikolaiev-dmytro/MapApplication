package com.example.mapapplication.model

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData

class LocationPermissionListener(private val context: Context):LiveData<Boolean>() {
    override fun onActive() {
        super.onActive()
        postValue(isLocationAccessAllowed())
    }
   private fun isLocationAccessAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(context,
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}