package com.example.mapapplication.ui.main

import androidx.lifecycle.ViewModel
import com.example.mapapplication.model.LocationPermissionListener
import com.example.mapapplication.repository.LocationRepository
import com.example.mapapplication.repository.PlacesRepository

class MainViewModel(
    private val placesRepository: PlacesRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {
    val locationPermissionListener: LocationPermissionListener =
        locationRepository.locationPermissionListener

}
