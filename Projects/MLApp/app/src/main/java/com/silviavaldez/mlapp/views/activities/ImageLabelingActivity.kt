package com.silviavaldez.mlapp.views.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.silviavaldez.mlapp.R
import com.silviavaldez.mlapp.helpers.CAMERA_REQUEST_CODE
import com.silviavaldez.mlapp.helpers.CameraHelper
import com.silviavaldez.mlapp.helpers.PermissionHelper
import com.silviavaldez.mlapp.helpers.UtilHelper
import kotlinx.android.synthetic.main.activity_image_labeling.*

class ImageLabelingActivity : AppCompatActivity() {

    private val classTag: String? = ImageLabelingActivity::class.simpleName
    private val cameraHelper: CameraHelper = CameraHelper(this)

    private var utilHelper: UtilHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_labeling)

        cameraHelper.setCamera(image_labelling_fab)
        utilHelper = UtilHelper(
            image_labelling_progress,
            image_labelling_fab,
            image_labelling_text_instruction,
            null,
            image_labelling_text_content
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionHelper.REQUEST_PERMISSIONS -> {
                val result = cameraHelper.openCamera(grantResults)

                if (result != 0) {
                    Snackbar.make(image_labelling_layout, result, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    utilHelper?.lockViews(true)

                    val photo = cameraHelper.showBitmap(image_labelling_image_photo)
                    if (photo != null) {
                        Snackbar.make(image_labelling_layout, R.string.msg_please_wait, Snackbar.LENGTH_LONG)
                            .show()

                        // TODO: Label objects
//                        labelImages(photo)
                    } else {
                        utilHelper?.showProgress(false)
                        Snackbar.make(image_labelling_layout, R.string.error_loading_picture, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}
