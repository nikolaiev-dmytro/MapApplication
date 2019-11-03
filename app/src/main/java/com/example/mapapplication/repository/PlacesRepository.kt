package com.example.mapapplication.repository

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.example.mapapplication.R
import com.example.mapapplication.model.Place
import com.example.mapapplication.model.Resource
import com.example.mapapplication.utils.DirectionJSONParser
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PlacesRepository(private val context: Context, private val apiService: PlacesApiService) {

    fun getPlaces(location: Location): MutableLiveData<Resource<List<Place>>> {
        val mutableData = MutableLiveData<Resource<List<Place>>>()
        mutableData.value = (Resource.loading(null))
        apiService.getNearbyPlaces(
            context.getString(R.string.places_api_key),
            "${location.latitude},${location.longitude}"
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ json ->
                if (json.has("status")) {
                    val status = json["status"].asString
                    if (status == "OK") {
                        if (json.has("results")) {
                            val places = Gson().fromJson<List<Place>>(
                                json["results"].asJsonArray,
                                object : TypeToken<List<Place>>() {}.type
                            )
                            if (places.isNotEmpty()) {
                                mutableData.value =
                                    (Resource.success(places.filterIndexed { index, place -> index < 10 }))
                            } else {
                                mutableData.value =
                                    Resource.error("Restaurants near you are not found", null)
                            }
                        }
                    } else {
                        if (json.has("error_message")) {
                            val message = json["error_message"].asString
                            mutableData.value = Resource.error(message, null)
                        }
                    }
                }

            }, { error ->
                mutableData.value = (Resource.error(error.localizedMessage, null))
            })
        return mutableData
    }

    fun getDirection(
        source: LatLng,
        dest: LatLng,
        observableData: MutableLiveData<Resource<List<LatLng>>>
    ) {
        observableData.value = (Resource.loading(null))
        apiService.getDirection(
            context.getString(R.string.places_api_key),
            "${source.latitude},${source.longitude}",
            "${dest.latitude},${dest.longitude}"
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ json ->
                if (json.has("status")) {
                    val status = json["status"].asString
                    if (status == "OK") {
                        val directionJSONParser = DirectionJSONParser()
                        val routes = directionJSONParser.parse(json)
                        if (routes.isEmpty()) {
                            observableData.value = Resource.error("Route not found", null)
                        } else {
                            val path = routes[0]
                            val routeList =
                                directionJSONParser.convertHashMapToRouteList(path)
                            observableData.value = Resource.success(routeList)
                        }
                    } else {
                        if (json.has("error_message")) {
                            val message = json["error_message"].asString
                            observableData.value = Resource.error(message, null)
                        }
                    }
                }
            }, { error ->
                observableData.value = Resource.error(error.localizedMessage, null)
            })
    }
}