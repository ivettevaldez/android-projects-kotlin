package com.ivettevaldez.coroutines.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.ivettevaldez.coroutines.R
import com.ivettevaldez.coroutines.common.BaseFragment

class HomeFragment : BaseFragment(), HomeArrayAdapter.Listener {

    override val screenTitle: String
        get() = "Coroutines Course"

    private lateinit var listScreensReachableFromHome: ListView
    private lateinit var adapterScreensReachableFromHome: HomeArrayAdapter

    companion object {

        fun newInstance(): Fragment = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        adapterScreensReachableFromHome = HomeArrayAdapter(requireContext(), this)

        listScreensReachableFromHome = view.findViewById(R.id.list_screens)
        listScreensReachableFromHome.adapter = adapterScreensReachableFromHome

        // Populate the adapter with the list of available screens
        adapterScreensReachableFromHome.addAll(*ScreenReachableFromHome.values())
        adapterScreensReachableFromHome.notifyDataSetChanged()

        return view
    }

    override fun onScreenClicked(screenReachableFromHome: ScreenReachableFromHome) {
        when (screenReachableFromHome) {
            ScreenReachableFromHome.UI_THREAD_DEMO -> screensNavigator.toUiThreadDemo()
            ScreenReachableFromHome.BACKGROUND_THREAD_DEMO -> screensNavigator.toBackgroundThreadDemo()
            ScreenReachableFromHome.BASIC_COROUTINES_DEMO -> screensNavigator.toBasicCoroutinesDemo()
            ScreenReachableFromHome.EXERCISE_1 -> screensNavigator.toExercise1()
        }
    }
}