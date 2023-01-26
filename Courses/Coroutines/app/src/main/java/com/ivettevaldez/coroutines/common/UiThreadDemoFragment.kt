package com.ivettevaldez.coroutines.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ivettevaldez.coroutines.R
import com.ivettevaldez.coroutines.home.ScreenReachableFromHome

class UiThreadDemoFragment : BaseFragment() {

    override val screenTitle: String
        get() = ScreenReachableFromHome.UI_THREAD_DEMO.description

    companion object {

        fun newInstance() = UiThreadDemoFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ui_thread_demo, container, false)
        return view
    }
}