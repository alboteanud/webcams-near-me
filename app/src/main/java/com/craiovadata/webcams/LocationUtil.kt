package com.craiovadata.webcams

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient

class LocationUtil {

    companion object {
        const val PERMISSIONS_REQUEST = 1

        fun getLocation(
            activity: Activity, fusedLocationClient: FusedLocationProviderClient?,
            callback: (location: Location?) -> Unit
        ) {

            // callback will be inside the activity's onRequestPermissionsResult(
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), PERMISSIONS_REQUEST
                )
                callback.invoke(null)
                return
            }

            if (fusedLocationClient == null) {
                callback.invoke(null)
                return
            }

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.

                callback.invoke(location)
            }
                .addOnCanceledListener {
                    callback.invoke(null)
                }
                .addOnFailureListener {
                    // Check GPS is enabled
                    val lm = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            snack("Please enable location services")
                        UiUtil.buildAlertMessageNoGps(activity)
                    }
                    callback.invoke(null)
                }

        }

    }
}