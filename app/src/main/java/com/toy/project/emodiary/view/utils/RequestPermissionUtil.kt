package com.toy.project.emodiary.view.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class RequestPermissionUtil(private val context: Context) {
    private val requestLocation = 1

    private val permissionsLocation = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(context, permissionsLocation[0]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, permissionsLocation[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, permissionsLocation, requestLocation)
        }
    }

    fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(context, permissionsLocation[0]) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, permissionsLocation[1]) == PackageManager.PERMISSION_GRANTED
    }
}