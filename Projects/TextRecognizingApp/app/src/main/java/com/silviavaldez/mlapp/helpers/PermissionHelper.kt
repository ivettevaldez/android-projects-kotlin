package com.silviavaldez.mlapp.helpers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

class PermissionHelper(private val activity: Activity) {

    companion object {
        const val REQUEST_PERMISSIONS = 2
    }

    fun validatePermissionResult(grantResults: IntArray): Boolean {
        var position = 0
        for (permission in grantResults) {
            if (grantResults[position] == PackageManager.PERMISSION_GRANTED) {
                position++
            } else {
                return false
            }
        }
        return true
    }

    fun checkAllPermissions(): Boolean {
        return if (checkCameraPermissions() && checkExternalStoragePermissions()) {
            true
        } else {
            // If there are no permissions granted, request them
            val permissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            ActivityCompat.requestPermissions(activity, permissions,
                REQUEST_PERMISSIONS
            )
            false
        }
    }

    private fun checkCameraPermissions(): Boolean {
        val camera = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
        return camera == PackageManager.PERMISSION_GRANTED
    }

    private fun checkExternalStoragePermissions(): Boolean {
        val writeExternal = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readExternal = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)

        return writeExternal == PackageManager.PERMISSION_GRANTED
                && readExternal == PackageManager.PERMISSION_GRANTED
    }

}