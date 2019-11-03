package com.example.mapapplication.repository

import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.example.mapapplication.R
import com.example.mapapplication.model.Place
import com.example.mapapplication.model.Resource
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
                            mutableData.value =
                                (Resource.success(places.filterIndexed { index, place -> index < 10 }))
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
}