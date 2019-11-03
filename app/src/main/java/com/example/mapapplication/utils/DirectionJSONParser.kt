package com.example.mapapplication.utils

import android.text.TextUtils
import com.google.android.gms.maps.model.LatLng
import org.json.JSONException
import com.google.gson.JsonArray
import com.google.gson.JsonObject


class DirectionJSONParser {

    fun parse(jsonObject: JsonObject): List<List<HashMap<String, String>>> {
        val routes = ArrayList<List<HashMap<String, String>>>()
        val jRoutes: JsonArray
        var jLegs: JsonArray
        var jSteps: JsonArray
        try {
            jRoutes = jsonObject.get("routes").asJsonArray
            for (i in 0 until jRoutes.size()) {
                jLegs = jRoutes.get(i).asJsonObject.get("legs").asJsonArray
                val path = ArrayList<HashMap<String, String>>()
                for (j in 0 until jLegs.size()) {
                    jSteps = jLegs.get(j).asJsonObject.get("steps").asJsonArray
                    for (k in 0 until jSteps.size()) {
                        var polyline = ""
                        polyline =
                            jSteps.get(k).asJsonObject.get("polyline").asJsonObject.get("points")
                                .asString
                        val list = decodePoly(polyline)
                        for (l in list.indices) {
                            val hasmap = HashMap<String, String>()
                            hasmap.put("lat", java.lang.Double.toString(list[l].latitude))
                            hasmap.put("lng", java.lang.Double.toString(list[l].longitude))

                            path.add(hasmap)
                        }
                        routes.add(path)
                    }
                }

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: Exception) {

        }

        return routes
    }

    private fun decodePoly(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }

    fun convertHashMapToRouteList(path: List<HashMap<String, String>>): List<LatLng> {
        val points = ArrayList<LatLng>()
        for (pointHashMap in path) {
            if (!TextUtils.isEmpty(pointHashMap["lat"]) && !TextUtils.isEmpty(pointHashMap["lng"])) {
                val point =
                    LatLng(pointHashMap["lat"]!!.toDouble(), pointHashMap["lng"]!!.toDouble())
                points.add(point)
            }
        }
        return points
    }
}