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
import com.ivettevaldez.coroutines.common.ThreadInfoLogger
import com.ivettevaldez.coroutines.home.ScreenReachableFromHome
import java.util.*

class ViewModelDemoFragment : BaseFragment() {

    override val screenTitle get() = ScreenReachableFromHome.DEMO_VIEW_MODEL.description

    private lateinit var textElapsedTime: TextView
    private lateinit var buttonTrackTime: Button

    private lateinit var myViewModel: MyViewModel

    companion object {

        @JvmStatic
        fun newInstance(): Fragment = ViewModelDemoFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myViewModel = ViewModelProvider(this, MyViewModelFactory()).get((MyViewModel::class.java))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_basic_coroutine_demo, container, false)

        textElapsedTime = view.findViewById(R.id.basic_coroutine_text_remaining_time)
        buttonTrackTime = view.findViewById(R.id.basic_coroutine_button_start)

        buttonTrackTime.setOnClickListener {
            logThreadInfo("Button callback")
            myViewModel.toggleTrackElapsedTime()
        }

        myViewModel.isTrackingTime.observe(viewLifecycleOwner, { isTrackingTime ->
            buttonTrackTime.text = if (isTrackingTime) {
                getString(R.string.action_stop_tracking)
            } else {
                getString(R.string.action_start_tracking)
            }
        })

        myViewModel.elapsedTime.observe(viewLifecycleOwner, { elapsedTime ->
            textElapsedTime.text = String.format(
                Locale.getDefault(),
                getString(R.string.template_elapsed_time_s),
                elapsedTime
            )
        })

        return view
    }

    override fun onStop() {
        logThreadInfo("onStop()")
        super.onStop()
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }
}