package com.silviavaldez.sampleapp.views.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.silviavaldez.sampleapp.R
import com.silviavaldez.sampleapp.helpers.CAMERA_REQUEST_CODE
import com.silviavaldez.sampleapp.helpers.CameraHelper
import com.silviavaldez.sampleapp.helpers.PermissionHelper
import com.silviavaldez.sampleapp.helpers.TypefaceHelper
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity() {

    private val cameraHelper: CameraHelper = CameraHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        cameraHelper.setCamera(camera_fab)
        setUpTypefaces()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionHelper.PERMISSION_ALL_CODE -> {
                val result = cameraHelper.openCamera(grantResults)

                if (result != 0) {
                    Snackbar.make(camera_layout, result, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val photo = cameraHelper.showBitmap(camera_image_photo)
                    if (photo != null) {
                        camera_text_instruction.visibility = View.GONE
                    } else {
                        Snackbar.make(camera_layout,
                                R.string.error_loading_picture,
                                Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setUpTypefaces() {
        val typefaceHelper = TypefaceHelper(this)
        typefaceHelper.setUpActionBar(title.toString())

        camera_text_instruction.typeface = typefaceHelper.bold
    }
}
