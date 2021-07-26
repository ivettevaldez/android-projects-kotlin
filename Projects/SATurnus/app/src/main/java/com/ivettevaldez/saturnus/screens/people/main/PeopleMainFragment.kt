package com.ivettevaldez.saturnus.screens.people.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.ivettevaldez.saturnus.screens.common.controllers.BaseFragment
import com.ivettevaldez.saturnus.screens.common.controllers.ControllerFactory
import com.ivettevaldez.saturnus.screens.common.viewsmvc.ViewMvcFactory
import javax.inject.Inject

class PeopleMainFragment : BaseFragment() {

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var controller: PeopleMainController

    companion object {

        @JvmStatic
        fun newInstance() = PeopleMainFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)

        controller = controllerFactory.newPeopleMainController()

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewMvc = viewMvcFactory.newPeopleMainViewMvc(parent)

        controller.bindView(viewMvc)

        // FIXME: Fix this dependency.
        val adapter = PeopleMainPagerAdapter(requireActivity() as AppCompatActivity)
        controller.initViewPager(adapter)

        return viewMvc.getRootView()
    }

    override fun onStart() {
        super.onStart()
        controller.onStart()
    }

    override fun onStop() {
        super.onStop()
        controller.onStop()
    }
}