package com.ivettevaldez.cleanarchitecture.common.permissions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ivettevaldez.cleanarchitecture.common.BaseObservable

class PermissionsHelper(private val activity: Activity) :
    BaseObservable<PermissionsHelper.Listener>() {

    interface Listener {

        fun onPermissionGranted(permission: String, requestCode: Int)
        fun onPermissionDeclined(permission: String, requestCode: Int)
        fun onPermissionDeclinedAndDontAskAgain(permission: String, requestCode: Int)
    }

    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (permissions.isEmpty()) {
            throw RuntimeException("No permissions in onRequestPermissionsResult")
        } else {
            val permission = permissions[0]
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                notifyPermissionGranted(permission, requestCode)
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    notifyPermissionDeclined(permission, requestCode)
                } else {
                    notifyPermissionDeclinedAndDontAskAgain(permission, requestCode)
                }
            }
        }
    }

    private fun notifyPermissionGranted(permission: String, requestCode: Int) {
        for (listener in getListeners()) {
            listener.onPermissionGranted(permission, requestCode)
        }
    }

    private fun notifyPermissionDeclined(permission: String, requestCode: Int) {
        for (listener in getListeners()) {
            listener.onPermissionDeclined(permission, requestCode)
        }
    }

    private fun notifyPermissionDeclinedAndDontAskAgain(permission: String, requestCode: Int) {
        for (listener in getListeners()) {
            listener.onPermissionDeclinedAndDontAskAgain(permission, requestCode)
        }
    }
}