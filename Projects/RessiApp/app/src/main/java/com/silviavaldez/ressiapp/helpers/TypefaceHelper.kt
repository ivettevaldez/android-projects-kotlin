package com.silviavaldez.ressiapp.helpers

import android.app.Activity
import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by Silvia Valdez on 13/02/2018.
 */
class TypefaceHelper(private val activity: Activity) {

    private val tag = TypefaceHelper::class.java.simpleName

    private val assets = activity.resources.assets

    private val robotoMediumPath = "fonts/Roboto-Medium.ttf"
    private val robotoRegularPath = "fonts/Roboto-Regular.ttf"
    private val robotoLightPath = "fonts/Roboto-Light.ttf"

    val robotoMedium: Typeface = Typeface.createFromAsset(assets, robotoMediumPath)
    val robotoRegular: Typeface = Typeface.createFromAsset(assets, robotoRegularPath)
    val robotoLight: Typeface = Typeface.createFromAsset(assets, robotoLightPath)

    /**
     * Overrides all fonts in a given layout with text of any kind in it.
     * The default font for the whole application is Roboto Regular.
     */
    fun overrideAllTypefaces() {
        try {
            val view = activity.window.decorView.rootView
            overrideTypefaces(view, robotoRegular)
        } catch (ex: Exception) {
            Log.e(tag, "Attempting to override all typefaces", ex)
        }
    }

    private fun overrideTypefaces(view: View, typeface: Typeface) {
        try {
            if (view is ViewGroup) {
                (0 until view.childCount)
                        .map { view.getChildAt(it) }
                        .forEach { overrideTypefaces(it, typeface) }
            } else if (view is TextView) {
                view.typeface = typeface
            }
        } catch (ex: Exception) {
            Log.e(tag, "Attempting to override typeface", ex)
        }
    }
}