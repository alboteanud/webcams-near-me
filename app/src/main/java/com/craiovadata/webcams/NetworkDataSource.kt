package com.craiovadata.webcams


import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.craiovadata.webcams.UiUtil.Companion.log
import com.craiovadata.webcams.model.Content

/**
 * Provides an API for doing all operations with the server data
 */
class NetworkDataSource(private val context: Context) {


    fun fetchWebcams(
        latitude: Double?,
        longitude: Double?,
        function: (items: Array<Content.Item>) -> Unit
    ) {
        if (latitude==null || longitude==null) {
            return
        }
        val weatherRequestUrl = getWebcamListUrl(context, latitude, longitude)
        getResponseFromHttpUrl(context, weatherRequestUrl) { jsonResponse ->
            val webcamList = WebcamJsonParser.parseWebcamsResponse(jsonResponse)
            log("webcams JSON has ${webcamList.size} values")
            function.invoke(webcamList)
        }
    }

    private fun getWebcamListUrl(
        mContext: Context,
        latitude: Double,
        longitude: Double
    ): String {
        val windyApiKey = mContext.getString(R.string.windy_api_key)
        val areaKm = mContext.getString(R.string.config_area_km_webcams)

//     if (inTestMode) return "https://api.windy.com/api/webcams/v2/list/orderby=popularity/nearby=44.33,23.73,$areaKm?key=$windyApiKey&show=webcams:location,image"  // Craiova
        // ordere by: polularity, distance, hotnes
        val url =
            "https://api.windy.com/api/webcams/v2/list/orderby=distance/nearby=$latitude,$longitude,$areaKm?key=$windyApiKey&show=webcams:location,image"
//        log(url)
        return url
    }

    fun getResponseFromHttpUrl(
        context: Context,
        urlString: String,
        callback: (response: String?) -> Unit
    ) {

// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)

// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, urlString,
            { response ->
                callback.invoke(response)
            },
            {
                log(" Volley request - That didn't work!")
                callback.invoke(null)
            })

// Add the request to the RequestQueue.
        queue.add(stringRequest)

    }

}





