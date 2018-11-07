package com.silviavaldez.sampleapp.helpers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

/**
 * Helper class for requesting explicit permissions.
 * Created by Silvia Valdez on 11/7/18.
 */
class PermissionHelper(val activity: Activity) {

    companion object {

        // These are app-defined int constants. The callback method gets the result of the requests.
        const val PERMISSION_ALL_CODE = 1000
        const val PERMISSION_LOCATION_CODE = 1001
        const val PERMISSION_WRITE_EXTERNAL_CODE = 1002
        const val PERMISSION_CAMERA_CODE = 1003
    }

    fun requestAllPermissions(): Boolean {
        return if (checkCameraPermissions()
                && checkLocationPermissions()
                && checkExternalStoragePermissions()) {
            true
        } else {
            // If there are missing granted permissions, request them
            val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_ALL_CODE)
            false
        }
    }

    fun requestLocationPermissions(request: Boolean): Boolean {
        return if (checkLocationPermissions()) {
            true
        } else {
            // If there are missing granted permissions, request them
            val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_LOCATION_CODE)
            false
        }
    }

    fun requestExternalStoragePermissions(): Boolean {
        return if (checkExternalStoragePermissions()) {
            true
        } else {
            // If there are missing granted permissions, request them
            val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_WRITE_EXTERNAL_CODE)
            false
        }
    }

    fun requestCameraPermissions(): Boolean {
        return if (checkCameraPermissions()) {
            true
        } else {
            // If there are missing granted permissions, request them
            val permissions = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_CAMERA_CODE)
            false
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkExternalStoragePermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCameraPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }
}