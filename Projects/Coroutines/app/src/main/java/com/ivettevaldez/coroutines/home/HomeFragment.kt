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
        get() = resources.getString(R.string.app_name)

    private lateinit var listScreensReachableFromHome: ListView
    private lateinit var adapterScreensReachableFromHome: HomeArrayAdapter

    companion object {

        @JvmStatic
        fun newInstance(): Fragment = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        adapterScreensReachableFromHome = HomeArrayAdapter(requireContext(), this)

        listScreensReachableFromHome = view.findViewById(R.id.home_list_screens)
        listScreensReachableFromHome.adapter = adapterScreensReachableFromHome

        adapterScreensReachableFromHome.addAll(*ScreenReachableFromHome.values())
        adapterScreensReachableFromHome.notifyDataSetChanged()

        return view
    }

    override fun onScreenClicked(screenReachableFromHome: ScreenReachableFromHome) {
        when (screenReachableFromHome) {
            ScreenReachableFromHome.DEMO_THREADS -> screensNavigator.toThreadsDemo()
            ScreenReachableFromHome.DEMO_BASIC_COROUTINE -> screensNavigator.toBasicCoroutineDemo()
            ScreenReachableFromHome.EXERCISE_1 -> screensNavigator.toExercise1()
            ScreenReachableFromHome.EXERCISE_2 -> screensNavigator.toExercise2()
            ScreenReachableFromHome.DEMO_SCOPE_CANCELLATION -> screensNavigator.toScopeCancellationDemo()
            ScreenReachableFromHome.DEMO_VIEW_MODEL -> screensNavigator.toViewModelDemo()
        }
    }
}