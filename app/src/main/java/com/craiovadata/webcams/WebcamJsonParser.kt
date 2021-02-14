package com.craiovadata.webcams

import com.craiovadata.webcams.model.Content
import org.json.JSONException
import org.json.JSONObject
import java.util.*

internal class WebcamJsonParser {

    companion object {
        private const val STATUS = "status"
        private const val STATUS_OK = "OK"

        @Throws(JSONException::class)
        private fun hasHttpError(forecastJson: JSONObject): Boolean {
            if (forecastJson.has(STATUS) && forecastJson.getString(STATUS) == STATUS_OK) return false
            return true
        }

        @Throws(JSONException::class)
        private fun fromJsonWebcams(webcamJson: JSONObject): Array<Content.Item> {
            val result = webcamJson.getJSONObject("result")
            val jsonWebcamArray = result.getJSONArray("webcams")
            val webcamEntries = mutableListOf<Content.Item>()

            for (i in 0 until jsonWebcamArray.length()) { // Get the JSON object representing the day
                val jsonWebcam = jsonWebcamArray.getJSONObject(i)
                val webcam = fromJsonWebcam(jsonWebcam, i)
//                weatherEntries[i] = weather
                webcamEntries.add(i, webcam)
            }
            return webcamEntries.toTypedArray()
        }

        @Throws(JSONException::class)
        private fun fromJsonWebcam(jsonWebcam: JSONObject, i: Int): Content.Item {
            val webcamId = jsonWebcam.getString("id")
//            val statusActive = jsonWebcam.getString("status") == "active"
            val title = jsonWebcam.getString("title")
            val imageObj = jsonWebcam.getJSONObject("image")
            val updateMilli = imageObj.getLong("update") * 1000
            val previewUrl = imageObj.getJSONObject("current").getString("preview")

            return Content.Item(webcamId, title, previewUrl, Date(updateMilli) )
        }

        @Throws(JSONException::class)
        fun parseWebcamsResponse(jsonString: String?): Array<Content.Item> {
            if (jsonString == null) return emptyArray()
            val webcamJson = JSONObject(jsonString)
            if (hasHttpError(webcamJson)) return emptyArray()

            return fromJsonWebcams(webcamJson)
        }


    }
}
