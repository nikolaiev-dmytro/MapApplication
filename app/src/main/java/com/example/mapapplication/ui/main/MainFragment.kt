package com.example.mapapplication.ui.main

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.mapapplication.R
import com.example.mapapplication.model.Place
import com.example.mapapplication.model.Resource
import com.example.mapapplication.repository.LocationRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.view_marker_info.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel



class MainFragment : Fragment(), OnMapReadyCallback {


    companion object {
        fun newInstance() = MainFragment()
    }

    private val mainViewModel: MainViewModel by viewModel()
    private val locationRepository: LocationRepository by inject()
    private var map: GoogleMap? = null
    private val markers: MutableList<Marker> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initGoogleMap(savedInstanceState)


    }
    override fun onResume() {
        mapView?.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        mapView?.onDestroy()
        map?.clear()
        map = null
    }

    private fun initGoogleMap(savedInstanceState: Bundle?) {
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map?.uiSettings?.isMyLocationButtonEnabled = false
        map?.uiSettings?.isRotateGesturesEnabled = true
        map?.uiSettings?.isScrollGesturesEnabled = true
        map?.uiSettings?.isScrollGesturesEnabledDuringRotateOrZoom = true
        map?.uiSettings?.isZoomGesturesEnabled = true
        map?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoContents(marker: Marker?): View {
                return getInfoWindow(marker)
            }

            override fun getInfoWindow(marker: Marker?): View {
                val place = marker?.tag as Place?
                val view =
                    LayoutInflater.from(context).inflate(R.layout.view_marker_info, null, false)
                view.place_name?.text = place?.name
                view.place_subtitle?.text = place?.vicinity
                view.direction_button?.setOnClickListener {
                    val source = LatLng(
                        locationRepository?.getLastKnownLocation()?.latitude ?: 0.0,
                        locationRepository?.getLastKnownLocation()?.longitude ?: 0.0
                    )
                    val dest = LatLng(
                        place?.geometry?.location?.lat ?: 0.0,
                        place?.geometry?.location?.lng ?: 0.0
                    )
                    mainViewModel.getDirectionButtonClicked(source, dest)
                }
                return view
            }

        })
        observeViewModelInitialization()
    }

    private fun observePlacesData() {
        mainViewModel.getPlacesDataObserver().observe(viewLifecycleOwner, Observer { source ->
            when (source.status) {
                Resource.Status.LOADING -> {
                    showLoader()
                }
                Resource.Status.SUCCESS -> {
                    hideLoader()
                    showMarkers(source.data ?: ArrayList())
                }
                Resource.Status.ERROR -> {
                    hideLoader()
                    showToast(source.message)
                }
            }
        })
    }

    private fun showMarkers(places: List<Place>) {
        markers.forEach { it.remove() }
        for (place in places) {
            initMarker(place)?.let { markers.add(it) }
        }
    }

    private fun initMarker(place: Place): Marker? {
        val markerOptions = MarkerOptions()
        val marker = map?.addMarker(
            markerOptions.position(
                LatLng(
                    place.geometry?.location?.lat ?: 0.0,
                    place.geometry?.location?.lng ?: 0.0
                )
            )
                .title(place.name)
        )
        marker?.tag = place
        return marker
    }

    private fun showToast(message: String?) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun hideLoader() {
        loader?.visibility = View.GONE
    }

    private fun showLoader() {
        loader?.visibility = View.VISIBLE
    }

    private fun observeViewModelInitialization() {
        mainViewModel.locationPermissionListener.observe(
            viewLifecycleOwner,
            Observer { permissionAccessed ->
                if (permissionAccessed) {
                    map?.isMyLocationEnabled = true
                    map?.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                locationRepository.getLastKnownLocation()?.latitude ?: 0.0,
                                locationRepository.getLastKnownLocation()?.longitude ?: 0.0
                            ), 14f
                        )
                    )
                    observePlacesData()
                } else {
                    RxPermissions(this).request(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ).subscribe()
                }
            })
    }

}
