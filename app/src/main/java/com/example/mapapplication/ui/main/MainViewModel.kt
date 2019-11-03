package com.example.mapapplication.ui.main

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapapplication.model.LocationPermissionListener
import com.example.mapapplication.model.Place
import com.example.mapapplication.model.Resource
import com.example.mapapplication.repository.LocationRepository
import com.example.mapapplication.repository.PlacesRepository
import com.google.android.gms.maps.model.LatLng

class MainViewModel(
    private val placesRepository: PlacesRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {
    val locationPermissionListener: LocationPermissionListener =
        locationRepository.locationPermissionListener
    private var placesData: MutableLiveData<Resource<List<Place>>>? = null


    fun getPlacesDataObserver(): LiveData<Resource<List<Place>>> {
        if (placesData == null) {
            loadPlaces()
        }
        return placesData!!
    }


    private fun loadPlaces() {
        val location = locationRepository.getLastKnownLocation()
        if (location != null) {
            placesData = placesRepository.getPlaces(location)
        } else {
            placesData = MutableLiveData()
            placesData?.value = Resource.error("Location is not found", null)
        }
    }

    fun getDirectionButtonClicked(source: LatLng, dest: LatLng) {

    }
}
