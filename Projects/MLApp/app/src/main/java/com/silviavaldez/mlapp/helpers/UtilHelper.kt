package com.silviavaldez.mlapp.helpers

import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView

class UtilHelper(
    private val progress: ProgressBar,
    private val fab: FloatingActionButton,
    private val textInstruction: TextView,
    private val textTitle: TextView?,
    private val textContent: TextView
) {

    fun showProgress(show: Boolean) {
        when {
            show -> {
                progress.visibility = View.VISIBLE
                fab.isEnabled = false
            }
            else -> {
                progress.visibility = View.GONE
                fab.isEnabled = true
            }
        }
    }

    fun lockViews(lock: Boolean) {
        if (lock) {
            showProgress(true)

            textInstruction.visibility = View.GONE
            textTitle?.visibility = View.GONE
            textContent.visibility = View.GONE
        } else {
            showProgress(false)

            textTitle?.visibility = View.VISIBLE
            textContent.visibility = View.VISIBLE
        }
    }
}