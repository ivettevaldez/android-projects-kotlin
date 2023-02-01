package com.ivettevaldez.coroutines.demos.viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ivettevaldez.coroutines.R
import com.ivettevaldez.coroutines.common.BaseFragment
import com.ivettevaldez.coroutines.home.ScreenReachableFromHome
import com.ivettevaldez.coroutines.common.ThreadInfoLogger as logger

class ViewModelDemoFragment : BaseFragment() {

    override val screenTitle: String
        get() = ScreenReachableFromHome.VIEW_MODEL_DEMO.description

    private lateinit var buttonStartTracking: Button
    private lateinit var textElapsedTime: TextView

    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, MyViewModelFactory())[MyViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_model_demo, container, false)

        textElapsedTime = view.findViewById(R.id.txt_elapsed_time)

        buttonStartTracking = view.findViewById(R.id.btn_start_tracking)
        buttonStartTracking.setOnClickListener {
            logger.logThreadInfo("button callback")
            viewModel.toggleTrackTime()
        }

        viewModel.elapsedTime.observe(viewLifecycleOwner) { elapsedTime ->
            textElapsedTime.text =
                getString(R.string.elapsed_time_sec_template, elapsedTime.toString())
        }

        viewModel.isTrackingTime.observe(viewLifecycleOwner) { isTrackingTime ->
            buttonStartTracking.text = if (isTrackingTime) {
                getString(R.string.stop_tracking)
            } else {
                getString(R.string.start_tracking)
            }
        }

        return view
    }

    override fun onStop() {
        logger.logThreadInfo("onStop()")
        super.onStop()
    }

    companion object {

        fun newInstance(): Fragment = ViewModelDemoFragment()
    }
}